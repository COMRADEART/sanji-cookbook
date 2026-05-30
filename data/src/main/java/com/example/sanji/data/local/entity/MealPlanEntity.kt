package com.example.sanji.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_plans")
data class MealPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val day: Int,
    val mealType: String, // "breakfast", "lunch", "dinner"
    val title: String,
    val description: String,
    val ingredients: String, // Comma-separated
    val instructions: String, // Pipe-separated
    val prepTime: String
)
