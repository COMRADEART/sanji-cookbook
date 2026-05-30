package com.example.sanji.domain.model

data class MealPlan(
    val id: Long = 0,
    val day: Int,
    val mealType: String,
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val prepTime: String
)
