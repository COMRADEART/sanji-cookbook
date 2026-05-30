from typing import Dict, Any, List

class MemoryModule:
    @staticmethod
    def build_user_context(profile: Dict[str, Any]) -> str:
        """
        Formulate a user profile prompt fragment.
        """
        if not profile:
            return "No specific dietary profile set. The user eats everything."
        
        context_parts = []
        if profile.get("name"):
            context_parts.append(f"User's name is {profile['name']}.")
        if profile.get("diet_type"):
            context_parts.append(f"Diet preference: {profile['diet_type']}.")
        if profile.get("allergies"):
            context_parts.append(f"Allergies/Restrictions: {profile['allergies']}.")
        if profile.get("favorite_ingredients"):
            context_parts.append(f"Favorite ingredients to cook with: {profile['favorite_ingredients']}.")
        if profile.get("disliked_ingredients"):
            context_parts.append(f"Ingredients to avoid: {profile['disliked_ingredients']}.")
        
        return " ".join(context_parts)

    @staticmethod
    def format_chat_history(history: List[Dict[str, str]]) -> str:
        """
        Formats list of chat dicts (sender, message) for LLM context.
        """
        if not history:
            return ""
        
        formatted = []
        for msg in history[-10:]: # Limit to last 10 messages for context window efficiency
            sender = "User" if msg.get("sender") == "user" else "Sanji"
            text = msg.get("text", "")
            formatted.append(f"{sender}: {text}")
        
        return "\n".join(formatted)
