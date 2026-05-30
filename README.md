# Sanji's Cookbook 👨‍🍳🍽️

A premium, interactive Android cookbook application inspired by the legendary chef of the Straw Hat Pirates, **Vinsmoke Sanji**.

## 📖 Table of Contents
- [🌟 Features](#-features)
- [🛠️ Tech Stack](#️-tech-stack)
- [🏗️ Architecture Overview](#️-architecture-overview)
- [🚀 Getting Started](#-getting-started)
- [🧪 Testing & Quality](#-testing--quality)
- [🔧 Troubleshooting](#-troubleshooting)
- [📜 License](#-license)

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

### Prerequisites
- **Android Studio**: Iguana (2023.2.1) or newer.
- **JDK**: 17 (embedded in Android Studio).
- **Android SDK**: API 34 (Compile SDK), API 24+ (Minimum SDK).
- **Gradle**: 8.13.2.

### Installation
1.  Clone the repository:
    ```bash
    git clone https://github.com/COMRADEART/sanji-cookbook.git
    ```
2.  Open the project in Android Studio.
3.  Sync Gradle and run the app.

## 🧪 Testing & Quality
The project includes a robust testing and linting setup:
- **Unit Tests**: Run `./gradlew test` to execute JUnit and Mockito tests.
- **Linting**:
  - **Ktlint**: Run `./gradlew ktlintCheck` for Kotlin style checks.
  - **Detekt**: Run `./gradlew detekt` for static code analysis.
- **CI/CD**: GitHub Actions automatically runs linting, tests, and builds an APK on every push.

## 🔧 Troubleshooting

### Gradle Sync Issues
- Ensure you have a stable internet connection.
- Clean and Rebuild project: `Build > Clean Project` followed by `Build > Rebuild Project`.
- Invalidate Caches: `File > Invalidate Caches...`.

### "Hilt" Compilation Errors
- Ensure the `kapt` plugin is correctly configured.
- Check if `hilt-android-compiler` version matches `hilt-android`.

### App Crashing on Start
- Check Logcat for any `Room` migration errors. The database schema might have changed without a migration (use `fallbackToDestructiveMigration` for development).

## 📜 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

One Piece is a trademark of Eiichiro Oda and Shueisha. This project is for educational and fan purposes.

---
*"A real cook never wastes a single scrap of food!"* — Sanji 💙
