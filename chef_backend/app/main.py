from fastapi import FastAPI, UploadFile, File, HTTPException
from pydantic import BaseModel
import whisper
import os
import json
import redis.asyncio as redis
from typing import List, Optional, Dict, Any
import google.generativeai as genai
from dotenv import load_dotenv
import uuid

# Import custom brain modules
from services.brain import BrainCoordinator
from services.vision import VisionModule
from services.recipe_gen import RecipeGenerator
from services.meal_planner import MealPlanner

# Google Cloud Monitoring & Logging
import google.cloud.logging
import googlecloudprofiler
from google.cloud import pubsub_v1

load_dotenv()

# GCP Project Config
PROJECT_ID = os.getenv("GOOGLE_CLOUD_PROJECT", "sanji-cookbook-ui")
TOPIC_ID = "dish-generation-tasks"

# Initialize Pub/Sub
try:
    publisher = pubsub_v1.PublisherClient()
    topic_path = publisher.topic_path(PROJECT_ID, TOPIC_ID)
except Exception:
    publisher = None
    topic_path = None

# Initialize Cloud Logging & Profiler (Only if explicitly enabled in GCP environment)
if os.getenv("ENABLE_GCP_SERVICES") == "true":
    try:
        logging_client = google.cloud.logging.Client()
        logging_client.setup_logging()
    except Exception:
        pass

    try:
        googlecloudprofiler.start(service="sanji-chef-backend", service_version="1.0.0")
    except Exception:
        pass

app = FastAPI(title="Sanji AI Chef Companion API")

# Initialize Redis
REDIS_HOST = os.getenv("REDIS_HOST", "localhost")
REDIS_PORT = int(os.getenv("REDIS_PORT", 6379))
cache = redis.Redis(
    host=REDIS_HOST, 
    port=REDIS_PORT, 
    decode_responses=True,
    socket_connect_timeout=0.5,
    socket_timeout=0.5,
    retry_on_timeout=False
)

redis_available = True

# Load Whisper model
whisper_model = whisper.load_model("base")

# Initialize Brain components
brain = BrainCoordinator()
vision_module = VisionModule()
recipe_gen = RecipeGenerator()
planner = MealPlanner()

class ChatRequest(BaseModel):
    message: str
    user_id: str
    history: Optional[List[Dict[str, str]]] = []
    profile: Optional[Dict[str, Any]] = None
    trust_level: Optional[int] = 50

class ChatResponse(BaseModel):
    response: str
    emotional_state: str
    trust_level: int
    chef_tips: Optional[List[str]] = None
    ui_command: Optional[Dict[str, Any]] = None

class GenerateRecipeRequest(BaseModel):
    ingredients: List[str]
    diet_type: Optional[str] = ""
    allergies: Optional[str] = ""
    user_message: Optional[str] = ""

class GenerateMealPlanRequest(BaseModel):
    diet_type: Optional[str] = ""
    allergies: Optional[str] = ""
    calorie_goal: Optional[str] = ""
    duration_days: Optional[int] = 3

@app.get("/")
async def root():
    return {"status": "Sanji is in the kitchen!"}

@app.post("/chat", response_model=ChatResponse)
async def chat(request: ChatRequest):
    global redis_available
    print(f"[CHAT] Received message: '{request.message}' from user '{request.user_id}'", flush=True)
    cache_key = f"chat:{request.user_id}:{request.message}:{request.trust_level}"
    if redis_available:
        try:
            print("[CHAT] Checking cache...", flush=True)
            cached_response = await cache.get(cache_key)
            if cached_response:
                print("[CHAT] Cache hit!", flush=True)
                return ChatResponse(**json.loads(cached_response))
        except Exception as ce:
            print(f"[CHAT] Redis cache error: {ce}. Disabling Redis cache.", flush=True)
            redis_available = False

    try:
        print("[CHAT] Querying brain coordinator...", flush=True)
        response_data = await brain.get_response(
            message=request.message,
            history=request.history or [],
            profile=request.profile or {},
            current_trust=request.trust_level or 50
        )
        print("[CHAT] Brain coordinator finished.", flush=True)
        
        chat_resp = ChatResponse(
            response=response_data["response"],
            emotional_state=response_data["emotional_state"],
            trust_level=response_data["trust_level"],
            chef_tips=response_data["chef_tips"],
            ui_command=response_data.get("ui_command")
        )

        # Store in cache for 10 minutes
        if redis_available:
            try:
                print("[CHAT] Saving to cache...", flush=True)
                await cache.set(cache_key, json.dumps(chat_resp.dict()), ex=600)
            except Exception as ce:
                print(f"[CHAT] Redis save error: {ce}. Disabling Redis cache.", flush=True)
                redis_available = False
        
        print("[CHAT] Returning response.", flush=True)
        return chat_resp
    except Exception as e:
        print(f"Error in chat endpoint: {e}", flush=True)
        return ChatResponse(
            response="Forgive me, I got a bit distracted by the aroma! What were we cooking?",
            emotional_state="Passionate",
            trust_level=request.trust_level or 50,
            chef_tips=["Always check your seasoning!"]
        )

@app.post("/transcribe")
async def transcribe_audio(file: UploadFile = File(...)):
    temp_filename = f"temp_{file.filename}"
    with open(temp_filename, "wb") as buffer:
        buffer.write(await file.read())
    
    try:
        result = whisper_model.transcribe(temp_filename)
        return {"text": result["text"], "language": result["language"]}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
    finally:
        if os.path.exists(temp_filename):
            os.remove(temp_filename)

@app.post("/recognize-ingredients")
async def recognize_ingredients(file: UploadFile = File(...)):
    try:
        image_bytes = await file.read()
        result = await vision_module.analyze_ingredients_image(image_bytes)
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Failed to analyze image: {e}")

@app.post("/generate-recipe")
async def generate_recipe(request: GenerateRecipeRequest):
    try:
        result = await recipe_gen.generate_adaptive_recipe(
            ingredients=request.ingredients,
            diet_type=request.diet_type,
            allergies=request.allergies,
            user_message=request.user_message
        )
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Failed to generate recipe: {e}")

@app.post("/generate-meal-plan")
async def generate_meal_plan(request: GenerateMealPlanRequest):
    try:
        result = await planner.generate_plan(
            diet_type=request.diet_type,
            allergies=request.allergies,
            calorie_goal=request.calorie_goal,
            duration_days=request.duration_days
        )
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Failed to generate meal plan: {e}")

@app.post("/generate-living-dish")
async def generate_living_dish(dish_name: str, motion_type: str = "zoom-in"):
    """
    Asynchronously starts a living dish generation job via Pub/Sub.
    Returns a job_id immediately.
    """
    job_id = str(uuid.uuid4())
    
    # Check cache first
    cache_key = f"dish:{dish_name}:{motion_type}"
    try:
        cached_dish = await cache.get(cache_key)
        if cached_dish:
            return {"job_id": "cached", "status": "completed", "data": json.loads(cached_dish)}
    except Exception:
        pass

    try:
        # Prepare task message
        task_data = {
            "job_id": job_id,
            "dish_name": dish_name,
            "motion_type": motion_type
        }
        message_json = json.dumps(task_data)
        message_bytes = message_json.encode("utf-8")

        # Publish to Pub/Sub
        try:
            future = publisher.publish(topic_path, message_bytes)
        except Exception:
            pass # Fallback logic would go here

        # Initialize job status in Redis
        await cache.set(f"job:{job_id}", json.dumps({"status": "processing"}), ex=3600)

        return {"job_id": job_id, "status": "queued"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/get-job-status/{job_id}")
async def get_job_status(job_id: str):
    """
    Checks the status of a dish generation job.
    """
    status_json = await cache.get(f"job:{job_id}")
    if not status_json:
        raise HTTPException(status_code=404, detail="Job not found")
    
    return json.loads(status_json)

# Worker Simulation Endpoint
@app.post("/worker/process-dish")
async def worker_process_dish(job_id: str, dish_name: str, motion_type: str):
    """
    Simulation of the Nano Banana rendering worker.
    """
    import asyncio
    await asyncio.sleep(5) # Simulate AI rendering time
    
    result = {
        "dish": dish_name,
        "motion": motion_type,
        "video_url": f"https://generated-assets.ai/living-dishes/{dish_name.replace(' ', '_')}_{motion_type}.mp4",
        "style": "One Piece Anime Style"
    }
    
    # Update job status and cache the final result
    await cache.set(f"job:{job_id}", json.dumps({"status": "completed", "data": result}), ex=3600)
    await cache.set(f"dish:{dish_name}:{motion_type}", json.dumps(result), ex=86400 * 7)
    
    return {"status": "success"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
