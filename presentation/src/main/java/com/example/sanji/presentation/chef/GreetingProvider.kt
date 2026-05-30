package com.example.sanji.presentation.chef

import java.util.Calendar

object GreetingProvider {

    fun getGreeting(userName: String, emotionalState: String): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val timeContext = when (hour) {
            in 5..11 -> "Morning"
            in 12..16 -> "Afternoon"
            in 17..21 -> "Evening"
            else -> "Night"
        }

        return when (timeContext) {
            "Morning" -> {
                if (emotionalState == "Mellorine") {
                    "Ah, $userName-san, the morning sun is almost as radiant as you! What shall we prepare for breakfast?"
                } else {
                    "Good morning, $userName. The kitchen is prepped. Shall we start with a nutritious breakfast?"
                }
            }
            "Afternoon" -> {
                if (emotionalState == "Mellorine") {
                    "Mellorine! $userName, the lunch rush is upon us! Let's whip up something spectacular to keep your energy high."
                } else {
                    "It's peak hour, $userName. What's on the menu for lunch? Let's make it efficient and delicious."
                }
            }
            "Evening" -> {
                if (emotionalState == "Mellorine") {
                    "A long day deserves a masterpiece for dinner, $userName. What are you craving tonight?"
                } else {
                    "Good evening, $userName. The ingredients are waiting. Shall we prepare a grand dinner?"
                }
            }
            else -> {
                if (emotionalState == "Mellorine") {
                    "A midnight snack for you, $userName? Only the finest for such a late hour."
                } else {
                    "Working late, $userName? A quick, sustaining meal might be just what you need."
                }
            }
        }
    }
}
