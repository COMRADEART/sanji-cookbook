package com.example.sanji.domain.usecase

import com.example.sanji.domain.model.Recipe
import com.example.sanji.domain.repository.RecipeRepository

class GetRecipesUseCase(private val repository: RecipeRepository) {
    suspend operator fun invoke(): List<Recipe> = repository.getRecipes()
}
