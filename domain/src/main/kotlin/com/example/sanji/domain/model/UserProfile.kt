package com.example.sanji.domain.model

data class UserProfile(
    val userId: String = "user_123",
    val name: String = "Luffy",
    val dietType: String = "",
    val allergies: String = "",
    val favoriteIngredients: String = "",
    val dislikedIngredients: String = "",
    val trustLevel: Int = 50
)
