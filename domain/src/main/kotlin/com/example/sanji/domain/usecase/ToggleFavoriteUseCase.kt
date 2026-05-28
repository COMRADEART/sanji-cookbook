package com.example.sanji.domain.usecase

import com.example.sanji.domain.repository.RecipeRepository

class ToggleFavoriteUseCase(private val repository: RecipeRepository) {
    suspend operator fun invoke(recipeId: String) = repository.toggleFavorite(recipeId)
}
