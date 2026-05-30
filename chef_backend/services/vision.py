import os
import json
from typing import Dict, Any, List
import google.generativeai as genai
from PIL import Image
import io

class VisionModule:
    def __init__(self):
        # Configure Gemini
        genai.configure(api_key=os.getenv("GEMINI_API_KEY"))
        # Using gemini-1.5-flash for fast multimodal processing
        self.model = genai.GenerativeModel('gemini-1.5-flash')

    async def analyze_ingredients_image(self, image_bytes: bytes) -> Dict[str, Any]:
        """
        Processes image bytes, queries Gemini to recognize ingredients, and suggests a recipe.
        """
        try:
            image = Image.open(io.BytesIO(image_bytes))
            
            prompt = """
            Look at this image of food ingredients.
            Identify all distinct raw or cooked food ingredients present in the picture.
            Also, suggest a culinary recipe that can be prepared primarily using these ingredients.
            
            Format your response strictly as a JSON object:
            {
              "ingredients": ["List of identified ingredient names"],
              "suggestion": "Suggested recipe name based on the ingredients"
            }
            Do not include markdown tags like ```json in the output. Just return the raw JSON string.
            """
            
            if not os.getenv("GEMINI_API_KEY"):
                raise ValueError("GEMINI_API_KEY is not configured")
            response = self.model.generate_content([image, prompt])
            
            content = response.text.strip()
            # Clean up potential markdown formatting if returned anyway
            if "```json" in content:
                content = content.split("```json")[1].split("```")[0].strip()
            elif "```" in content:
                content = content.split("```")[1].split("```")[0].strip()
                
            data = json.loads(content)
            return {
                "ingredients": data.get("ingredients", []),
                "suggestion": data.get("suggestion", "A custom chef creation!")
            }
        except Exception as e:
            # Fallback in case of model issues or format failures
            print(f"Error in VisionModule: {e}")
            return {
                "ingredients": ["Tomato", "Onion", "Shrimp"],
                "suggestion": "Baratie's Seafood Soup (Vision Server Fallback)"
            }
