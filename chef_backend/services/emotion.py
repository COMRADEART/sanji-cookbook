import re
from typing import Tuple

class EmotionalEngine:
    @staticmethod
    def evaluate_state(message: str, current_trust: int = 50) -> Tuple[str, int]:
        """
        Processes a user message and returns: (emotional_state, updated_trust)
        Emotional states: Passionate, Focused, Mellorine, Strict, Encouraging, Creative
        Trust ranges from 0 to 100.
        """
        msg_lower = message.lower()
        new_trust = current_trust
        state = "Passionate"

        # 1. Strict Triggers (wasting food, disrespecting ingredients, poor hygiene)
        waste_words = ["waste", "throw away", "toss out", "discard", "bin", "trash", "scrap", "leftover in trash"]
        hygiene_words = ["dirty hands", "no wash", "raw chicken on counter", "expired", "moldy"]
        
        is_wasteful = any(word in msg_lower for word in waste_words)
        is_unhygienic = any(word in msg_lower for word in hygiene_words)

        if is_wasteful:
            state = "Strict"
            new_trust = max(0, current_trust - 15)
        elif is_unhygienic:
            state = "Strict"
            new_trust = max(0, current_trust - 10)

        # 2. Mellorine Triggers (chivalrous terms, complimenting, female focus)
        elif any(x in msg_lower for x in ["mellorine", "lady", "beautiful", "swan", "love you", "nami", "robin"]):
            state = "Mellorine"
            new_trust = min(100, current_trust + 5)

        # 3. Focused Triggers (asking for direct, step-by-step instructions or hard technique questions)
        elif any(x in msg_lower for x in ["how do i", "step by step", "timer", "technique", "knife", "simmer", "sear"]):
            state = "Focused"
            new_trust = min(100, current_trust + 2)

        # 4. Encouraging Triggers (user made a mistake, burnt food, or is tired)
        elif any(x in msg_lower for x in ["burnt", "burned", "messed up", "ruined", "failed", "tired", "hungry"]):
            state = "Encouraging"
            new_trust = min(100, current_trust + 3)

        # 5. Creative Triggers (asking for new ideas, substitution, weird ingredients)
        elif any(x in msg_lower for x in ["substitute", "replace", "creative", "new idea", "experiment", "invent"]):
            state = "Creative"
            new_trust = min(100, current_trust + 2)
            
        # 6. Basic Compliments
        elif any(x in msg_lower for x in ["great chef", "best", "thank you", "thanks", "tasty", "delicious"]):
            state = "Passionate"
            new_trust = min(100, current_trust + 4)

        return state, new_trust
