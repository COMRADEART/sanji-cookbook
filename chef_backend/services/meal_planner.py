import os
import json
from typing import Dict, Any, List
import google.generativeai as genai

class MealPlanner:
    def __init__(self):
        genai.configure(api_key=os.getenv("GEMINI_API_KEY"))
        self.model = genai.GenerativeModel('gemini-1.5-flash')

    async def generate_plan(
        self,
        diet_type: str = "",
        allergies: str = "",
        calorie_goal: str = "",
        duration_days: int = 3
    ) -> Dict[str, Any]:
        """
        Generates a custom meal plan and aggregated shopping list based on user health parameters.
        """
        prompt = f"""
        You are Vinsmoke Sanji, the nutritionist chef of the Straw Hat Pirates.
        Generate a healthy, delicious meal plan for a duration of {duration_days} days.
        
        CONSTRAINTS:
        - Diet Type: {diet_type}
        - Allergies/Restrictions to avoid: {allergies}
        - Daily Calorie Target: {calorie_goal} calories
        
        Provide the response strictly as a JSON object containing:
        - "duration_days": integer
        - "diet_type": string
        - "daily_plan": List of days. Each day object must contain:
          - "day": integer (1, 2, 3...)
          - "breakfast": {{ "title": "string", "description": "string", "ingredients": ["ing 1", "ing 2"], "instructions": ["step 1", "step 2"], "prepTime": "string" }}
          - "lunch": {{ "title": "string", "description": "string", "ingredients": ["ing 1", "ing 2"], "instructions": ["step 1", "step 2"], "prepTime": "string" }}
          - "dinner": {{ "title": "string", "description": "string", "ingredients": ["ing 1", "ing 2"], "instructions": ["step 1", "step 2"], "prepTime": "string" }}
        - "grocery_list": List of all unique ingredients consolidated from all meals across all days.
        
        Do not include markdown code block tags like ```json. Return only the raw JSON string.
        """

        try:
            if not os.getenv("GEMINI_API_KEY"):
                raise ValueError("GEMINI_API_KEY is not configured")
            response = self.model.generate_content(prompt)
            content = response.text.strip()
            
            if "```json" in content:
                content = content.split("```json")[1].split("```")[0].strip()
            elif "```" in content:
                content = content.split("```")[1].split("```")[0].strip()
                
            data = json.loads(content)
            return data
        except Exception as e:
            print(f"Error in MealPlanner: {e}")
            # Fallback mock meal plan
            return {
                "duration_days": duration_days,
                "diet_type": diet_type if diet_type else "Balanced",
                "daily_plan": [
                    {
                        "day": day,
                        "breakfast": {
                            "title": "Mellorine Fruit Parfait",
                            "description": "Greek yogurt topped with seasonal fruits, honey, and fresh mint.",
                            "ingredients": ["Greek yogurt", "Mixed berries", "Honey", "Mint leaves"],
                            "instructions": ["Layer yogurt and berries in a glass.", "Drizzle honey on top.", "Garnish with mint."],
                            "prepTime": "5 mins"
                        },
                        "lunch": {
                            "title": "Baratie Tuna Melt",
                            "description": "Rich tuna salad with toasted sourdough and melted cheddar.",
                            "ingredients": ["Canned tuna", "Mayonnaise", "Sourdough bread", "Cheddar cheese", "Celery"],
                            "instructions": ["Mix tuna, mayonnaise, and diced celery.", "Spread on bread and top with cheese.", "Toast under broiler until cheese melts."],
                            "prepTime": "15 mins"
                        },
                        "dinner": {
                            "title": "All Blue Garlic Salmon",
                            "description": "Pan-seared salmon fillet glazed with butter, garlic, and fresh dill.",
                            "ingredients": ["Salmon fillet", "Garlic", "Butter", "Lemon juice", "Fresh dill"],
                            "instructions": ["Sear salmon in hot butter.", "Add minced garlic and baste salmon.", "Finish with lemon juice and dill."],
                            "prepTime": "20 mins"
                        }
                    }
                    for day in range(1, duration_days + 1)
                ],
                "grocery_list": [
                    "Greek yogurt", "Mixed berries", "Honey", "Mint leaves",
                    "Canned tuna", "Mayonnaise", "Sourdough bread", "Cheddar cheese", "Celery",
                    "Salmon fillet", "Garlic", "Butter", "Lemon juice", "Fresh dill"
                ]
            }
