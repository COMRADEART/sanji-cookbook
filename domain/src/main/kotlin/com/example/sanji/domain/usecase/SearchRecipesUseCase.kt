package com.example.sanji.domain.usecase

import com.example.sanji.domain.model.Recipe
import com.example.sanji.domain.repository.RecipeRepository

class SearchRecipesUseCase(private val repository: RecipeRepository) {
    suspend operator fun invoke(query: String, category: String? = null): List<Recipe> = 
        repository.searchRecipes(query, category)
}
