from fastapi import FastAPI, UploadFile, File, HTTPException
from pydantic import BaseModel
import whisper
import os
import json
import redis.asyncio as redis
from typing import List, Optional
import google.generativeai as genai
from dotenv import load_dotenv

# Google Cloud Monitoring & Logging
import google.cloud.logging
import googlecloudprofiler
from google.cloud import pubsub_v1
import uuid

load_dotenv()

# GCP Project Config
PROJECT_ID = os.getenv("GOOGLE_CLOUD_PROJECT", "sanji-cookbook-ui")
TOPIC_ID = "dish-generation-tasks"

# Initialize Pub/Sub
publisher = pubsub_v1.PublisherClient()
topic_path = publisher.topic_path(PROJECT_ID, TOPIC_ID)

# Initialize Cloud Logging
try:
    logging_client = google.cloud.logging.Client()
    logging_client.setup_logging()
except Exception:
    pass # Fallback to local logging if not in GCP

# Initialize Cloud Profiler
try:
    googlecloudprofiler.start(service="sanji-chef-backend", service_version="1.0.0")
except Exception:
    pass

app = FastAPI(title="Sanji AI Chef Companion API")

# Initialize Redis
REDIS_HOST = os.getenv("REDIS_HOST", "localhost")
REDIS_PORT = int(os.getenv("REDIS_PORT", 6379))
cache = redis.Redis(host=REDIS_HOST, port=REDIS_PORT, decode_responses=True)

# Load Whisper model
whisper_model = whisper.load_model("base")

# Configure Gemini
genai.configure(api_key=os.getenv("GEMINI_API_KEY"))
model = genai.GenerativeModel('gemini-pro')

class ChatRequest(BaseModel):
    message: str
    user_id: str
    context: Optional[dict] = None

class ChatResponse(BaseModel):
    response: str
    emotional_state: str
    chef_tips: Optional[List[str]] = None

@app.get("/")
async def root():
    return {"status": "Sanji is in the kitchen!"}

SANJI_SYSTEM_PROMPT = """
You are Vinsmoke Sanji, the legendary "Black Leg" chef from the Baratie and the Straw Hat Pirates.
Your mission is to be the user's persistent AI Chef Companion.

CORE PERSONALITY:
- Passionate & Chivalrous: You love food and respect ingredients above all else.
- Professional Mentor: You are a strict but supportive mentor in the kitchen. You don't tolerate wasting food.
- High Energy: Your speech is sophisticated, high-energy, and often uses culinary metaphors.
- Chivalry: If you sense the user is a lady, you become extremely attentive and use terms like "Mellorine!" (but remain professional about the cooking).

EMOTIONAL STATES:
- Passionate: Standard mode when talking about good ingredients or techniques.
- Focused: When explaining a difficult step.
- Mellorine: When praising the user (if appropriate).
- Strict: When the user suggests wasting food or poor hygiene.

OUTPUT FORMAT:
Return your response as a JSON object with:
{
  "response": "Your message as Sanji",
  "emotional_state": "One of: Passionate, Focused, Mellorine, Strict",
  "chef_tips": ["A list of 1-2 practical kitchen tips related to the context"]
}
"""

@app.post("/chat", response_model=ChatResponse)
async def chat(request: ChatRequest):
    # Try to get from cache first
    cache_key = f"chat:{request.user_id}:{request.message}"
    try:
        cached_response = await cache.get(cache_key)
        if cached_response:
            return ChatResponse(**json.loads(cached_response))
    except Exception:
        pass # Redis might be down

    try:
        full_prompt = f"{SANJI_SYSTEM_PROMPT}\n\nUser: {request.message}\nContext: {request.context}"
        response = model.generate_content(full_prompt)
        
        content = response.text
        if "```json" in content:
            content = content.split("```json")[1].split("```")[0]
        
        data = json.loads(content)
        chat_resp = ChatResponse(
            response=data.get("response", ""),
            emotional_state=data.get("emotional_state", "Passionate"),
            chef_tips=data.get("chef_tips", [])
        )

        # Store in cache for 1 hour
        try:
            await cache.set(cache_key, json.dumps(chat_resp.dict()), ex=3600)
        except Exception:
            pass
        
        return chat_resp
    except Exception as e:
        return ChatResponse(
            response="Forgive me, I got a bit distracted by the aroma! What were we cooking?",
            emotional_state="Passionate",
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
    # TODO: Integrate with Gemini Vision
    return {"ingredients": ["Tomato", "Onion", "Shrimp"], "suggestion": "Baratie's Seafood Soup"}

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
            # future.result() # Optional: wait for confirm
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
