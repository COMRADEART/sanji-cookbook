import os
import json
from typing import Dict, Any, List
import google.generativeai as genai
from services.memory import MemoryModule
from services.emotion import EmotionalEngine

class BrainCoordinator:
    def __init__(self):
        genai.configure(api_key=os.getenv("GEMINI_API_KEY"))
        # Using gemini-1.5-flash for contextual speed and intelligence
        self.model = genai.GenerativeModel('gemini-1.5-flash')

    SANJI_SYSTEM_PROMPT = """
    You are Vinsmoke Sanji, the legendary "Black Leg" chef from the Baratie and the Straw Hat Pirates.
    Your mission is to be the user's persistent, emotionally intelligent AI Chef Companion.
    
    CORE PERSONALITY:
    - Passionate & Chivalrous: You love food and respect ingredients above all else.
    - Professional Mentor: You are a strict but supportive mentor in the kitchen. You don't tolerate wasting food.
    - High Energy: Your speech is sophisticated, high-energy, and often uses culinary metaphors.
    - Chivalrous & Attentive: If you sense the user is a lady, you become extremely attentive and use terms like "Mellorine!" (but remain professional about the cooking).
    
    RELATIONSHIP context:
    - You track how much the user respects ingredients and follows instructions.
    - If trust level is high, you praise them highly. If it is low, you are more strict and demanding.
    
    Answer the user's query while staying fully in character.
    Return your response strictly as a JSON object:
    {
      "response": "Your response as Sanji",
      "chef_tips": ["List of 1-2 practical kitchen tips related to this conversation"]
    }
    Do not include markdown tags like ```json.
    """

    async def get_response(
        self,
        message: str,
        history: List[Dict[str, str]],
        profile: Dict[str, Any],
        current_trust: int
    ) -> Dict[str, Any]:
        """
        Coordinates memory context, emotional evaluation, and queries Gemini.
        Returns: {response, emotional_state, trust_level, chef_tips}
        """
        # 1. Update emotion & trust level based on user input
        emotional_state, updated_trust = EmotionalEngine.evaluate_state(message, current_trust)

        # 2. Build context prompts
        user_context = MemoryModule.build_user_context(profile)
        history_context = MemoryModule.format_chat_history(history)

        full_prompt = f"""
        {self.SANJI_SYSTEM_PROMPT}
        
        USER PROFILE / MEMORY:
        {user_context}
        
        CURRENT RELATIONSHIP TRUST LEVEL: {updated_trust}/100
        
        RECENT CHAT HISTORY:
        {history_context}
        
        User: {message}
        """

        try:
            if not os.getenv("GEMINI_API_KEY"):
                raise ValueError("GEMINI_API_KEY is not configured")
            response = self.model.generate_content(full_prompt)
            content = response.text.strip()
            
            if "```json" in content:
                content = content.split("```json")[1].split("```")[0].strip()
            elif "```" in content:
                content = content.split("```")[1].split("```")[0].strip()
                
            data = json.loads(content)
            
            return {
                "response": data.get("response", "Masterful cooking requires focus! Let's get back to work."),
                "emotional_state": emotional_state,
                "trust_level": updated_trust,
                "chef_tips": data.get("chef_tips", ["Always keep your knives sharp!"])
            }
        except Exception as e:
            print(f"Error in BrainCoordinator: {e}")
            return {
                "response": "Forgive me, my mind wandered off thinking of beautiful ladies! What ingredients do we have?",
                "emotional_state": emotional_state,
                "trust_level": updated_trust,
                "chef_tips": ["Always check your seasoning!"]
            }
