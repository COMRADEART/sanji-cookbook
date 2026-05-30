# Sanji's Cookbook: AI Culinary Mentor 👨‍🍳🔥

Welcome to the **Baratie**. This is no longer just a recipe app—it has been transformed into a **Conversational-First AI Culinary Mentor**, powered by a Python generative AI backend and a stunning Jetpack Compose Generative UI frontend. 

Instead of browsing generic menus, you interact directly with **Virtual Sanji**, an emotionally intelligent AI Chef that understands your tastes, teaches you technique, and dynamically generates the UI you need, when you need it.

## 🌟 The "Virtual Sanji" Experience

### 1. Conversational-First Interface (Generative UI)
*   **No More Static Menus:** The app boots directly into a chat with Sanji. Ask him what to cook, what to buy, or for a meal plan, and he dynamically generates rich UI components (Glass Overlays, Recipe Carousels) directly inside the chat.
*   **Emotional Intelligence:** Sanji tracks your rapport. Treat ingredients with respect, and he praises you. Waste food, and he becomes strict. His avatar and the UI's glow change dynamically based on his emotional state (*Mellorine, Focused, Strict, Passionate*).

### 2. Think Like a Chef (AI Backend)
*   **Autonomous Recipe Generation:** Give Sanji a list of ingredients or dietary restrictions, and he constructs a custom masterpiece from scratch.
*   **Visual Ingredient Recognition:** Snap a photo of your fridge. Sanji's Vision Module will identify the ingredients and immediately suggest what you can cook.
*   **Smart Meal Planning:** Ask for a 3-day meal plan, and Sanji will build the menu and automatically populate your Provisions List.

### 3. Interactive Culinary Teaching (Pro Cook Mode)
*   **The Kitchen is a Battlefield:** Step into the "Pro" cooking flow (Prep Area -> Cutting Board -> Stove). 
*   **Staggered Orchestration:** Ingredients and tasks arrive in a rhythmic, staggered sequence with spring-physics animations, mirroring the intensity of a professional kitchen service.
*   **Chef's Notes:** While cooking, Sanji provides real-time, context-aware tips to elevate your technique.

## 🛠️ Tech Stack

### 📱 Frontend (Android)
*   **Language:** Kotlin
*   **UI Framework:** Jetpack Compose (Material Design 3 + Custom "Baratie Heritage" Design System)
*   **Architecture:** Clean Architecture + MVVM + Generative UI Overlays
*   **Dependency Injection:** Hilt
*   **Local Storage:** Room Database

### 🧠 Backend (Python AI Brain)
*   **API Framework:** FastAPI
*   **AI Orchestration:** Google Gemini (1.5 Flash), LangChain, Whisper (Voice-to-Text)
*   **Caching & State:** Redis (Async)
*   **Asynchronous Jobs:** Google Cloud Pub/Sub

## 🏗️ Architecture Overview

### Generative UI Pipeline
1.  **User Input:** Voice or text is sent to the FastAPI backend.
2.  **Brain Coordinator:** Evaluates emotional state, updates trust, and queries the LLM with strict JSON formatting instructions.
3.  **UI Command Emission:** The LLM returns Sanji's conversational response alongside a `ui_command` (e.g., `{"action": "show_recipe_carousel", "recipe_ids": ["1", "3"]}`).
4.  **Frontend Rendering:** The Android app receives the JSON payload, displays the dialogue, and instantly animates a glass-like Compose popup over the chat.

## 🚀 Getting Started

### 1. Start the Backend
```bash
cd chef_backend
python -m venv venv
source venv/bin/activate # Windows: venv\Scripts\activate
pip install -r requirements.txt
# Ensure GEMINI_API_KEY is in your .env file
python -m app.main
```

### 2. Start the Android App
Open the project in Android Studio (Iguana or newer). Ensure you are running on an emulator connecting to `10.0.2.2` or update the API base URL in `di/AppModule.kt`. Run `assembleDebug`.

## 📜 License
This project is licensed under the MIT License.
One Piece is a trademark of Eiichiro Oda and Shueisha. This project is for educational and fan purposes.

---
*"A real cook never wastes a single scrap of food!"* — Sanji 💙
