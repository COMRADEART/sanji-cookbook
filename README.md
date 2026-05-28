# Sanji's Cookbook 👨‍🍳🍽️

A premium, interactive Android cookbook application inspired by the legendary chef of the Straw Hat Pirates, **Vinsmoke Sanji**. This app is designed for both One Piece enthusiasts and aspiring chefs who want a professional, feature-rich cooking companion.

## 🌟 Features

### 1. Interactive Home Screen
*   **Smooth Animations:** Tap the dish for a delightful "serving" animation powered by Jetpack Compose.
*   **Sanji's Chef Tips:** Get random culinary wisdom and Baratie philosophy directly from the chef himself.

### 2. Intelligent Recipe Search
*   **Quick Search:** Find recipes by name or description.
*   **Category Filtering:** Sort through Appetizers, Seafood, Noodles, and Desserts using Material 3 chips.
*   **One Piece Classics:** Pre-loaded with iconic recipes like *Baratie's Soup*, *Macedoine of Apple*, and *Sanji's Wano Soba*.

### 3. Professional Cook Mode
*   **Focused View:** A step-by-step UI to help you stay focused on the current instruction.
*   **Integrated Timers:** Start countdown timers directly from instructions (e.g., "Simmer for 5 minutes").
*   **Ingredient Checklist:** Cross off items as you prep them.

### 4. Kitchen Management
*   **Smart Grocery List:** Add ingredients from any recipe to a persistent shopping list with one tap.
*   **Favorites:** Save your favorite dishes for quick access, persisted locally via Room.
*   **Custom Recipes:** Add and manage your own secret recipes within the app.

## 🛠️ Tech Stack

*   **Language:** [Kotlin](https://kotlinlang.org/)
*   **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material Design 3
*   **Architecture:** Clean Architecture (Domain, Data, Presentation)
*   **Dependency Injection:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
*   **Local Database:** [Room](https://developer.android.com/training/data-storage/room)
*   **Navigation:** Jetpack Navigation Compose
*   **Async/Reactive:** Coroutines & Flow
*   **Image Loading:** [Coil](https://coil-kt.github.io/coil/)
*   **Animations:** Compose Animations & [Lottie](https://airbnb.io/lottie/) (support ready)

## 🏗️ Architecture Overview

The project follows **Clean Architecture** principles to ensure scalability, testability, and separation of concerns:

*   **`:domain`**: Contains pure Kotlin business logic, Models, and Repository interfaces.
*   **`:data`**: Implements repositories and handles data persistence (Room) and mock data sources.
*   **`:presentation`**: All UI code, ViewModels, and navigation logic.
*   **`:core`**: Shared utilities and base classes.
*   **`:app`**: The Android application module that wires everything together using Hilt.

## 🚀 Getting Started

1.  Clone the repository:
    ```bash
    git clone https://github.com/COMRADEART/sanji-cookbook.git
    ```
2.  Open the project in **Android Studio (Iguana or newer)**.
3.  Sync Gradle and run the app on an emulator or physical device (API 24+).

## 📜 License

This project is created for educational and fan purposes. One Piece is a trademark of Eiichiro Oda and Shueisha.

---
*"A real cook never wastes a single scrap of food!"* — Sanji 💙
