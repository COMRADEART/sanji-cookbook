package com.example.sanji.domain.usecase

import com.example.sanji.domain.model.Recipe
import com.example.sanji.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class ObserveFavoritesUseCase(private val repository: RecipeRepository) {
    operator fun invoke(): Flow<List<Recipe>> = repository.observeFavorites()
}
