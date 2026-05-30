package com.example.sanji.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val userId: String = "user_123",
    val name: String = "Luffy",
    val dietType: String = "",
    val allergies: String = "",
    val favoriteIngredients: String = "",
    val dislikedIngredients: String = "",
    val trustLevel: Int = 50
)
