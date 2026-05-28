package com.example.sanji.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecipeDto(
    val id: String,
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val imageUrl: String,
    val prepTime: String,
    val category: String,
    val tags: List<String> = emptyList(),
    val chefTips: List<String> = emptyList()
)
