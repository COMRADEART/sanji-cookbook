package com.example.sanji.domain.usecase

import com.example.sanji.domain.model.Recipe
import com.example.sanji.domain.repository.RecipeRepository

class GetRecipeByIdUseCase(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(id: String): Recipe? {
        return repository.getRecipeById(id)
    }
}
