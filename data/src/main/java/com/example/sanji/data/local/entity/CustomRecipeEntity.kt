package com.example.sanji.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_recipes")
data class CustomRecipeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val ingredients: String, // Stored as comma-separated or JSON string
    val instructions: String,
    val imageUrl: String,
    val prepTime: String,
    val category: String
)
