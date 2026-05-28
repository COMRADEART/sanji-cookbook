from fastapi import FastAPI, UploadFile, File, HTTPException
from pydantic import BaseModel
import whisper
import os
from typing import List, Optional

app = FastAPI(title="Sanji AI Chef Companion API")

# Load Whisper model (base for balance of speed/accuracy)
# In production, this might be pre-loaded or run in a background worker
whisper_model = whisper.load_model("base")

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

import google.generativeai as genai
from dotenv import load_dotenv

load_dotenv()

# Configure Gemini
genai.configure(api_key=os.getenv("GEMINI_API_KEY"))
model = genai.GenerativeModel('gemini-pro')

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
    try:
        # Construct the full prompt
        full_prompt = f"{SANJI_SYSTEM_PROMPT}\n\nUser: {request.message}\nContext: {request.context}"
        
        # Call Gemini
        response = model.generate_content(full_prompt)
        
        # Extract JSON from response (handling potential markdown blocks)
        content = response.text
        if "```json" in content:
            content = content.split("```json")[1].split("```")[0]
        
        import json
        data = json.loads(content)
        
        return ChatResponse(
            response=data.get("response", ""),
            emotional_state=data.get("emotional_state", "Passionate"),
            chef_tips=data.get("chef_tips", [])
        )
    except Exception as e:
        # Fallback if AI fails
        return ChatResponse(
            response="Forgive me, I got a bit distracted by the aroma! What were we cooking?",
            emotional_state="Passionate",
            chef_tips=["Always check your seasoning!"]
        )

@app.post("/transcribe")
async def transcribe_audio(file: UploadFile = File(...)):
    # Save temp file
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

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
