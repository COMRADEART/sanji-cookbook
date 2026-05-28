package com.example.sanji.domain.model

data class GroceryItem(
    val id: String,
    val name: String,
    val recipeId: String? = null,
    val isBought: Boolean = false
)
