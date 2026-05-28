package com.example.sanji.domain.repository

import com.example.sanji.domain.model.Recipe
import com.example.sanji.domain.model.GroceryItem
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun getRecipes(): List<Recipe>
    suspend fun getRecipeById(id: String): Recipe?
    suspend fun toggleFavorite(recipeId: String)
    fun observeFavorites(): Flow<List<Recipe>>
    suspend fun searchRecipes(query: String, category: String? = null): List<Recipe>
    
    // Grocery List
    fun observeGroceryList(): Flow<List<GroceryItem>>
    suspend fun addGroceryItem(item: GroceryItem)
    suspend fun toggleGroceryItem(id: String)
    suspend fun removeGroceryItem(id: String)
    suspend fun clearBoughtItems()

    // Custom Recipes
    suspend fun addCustomRecipe(recipe: Recipe)
    fun observeCustomRecipes(): Flow<List<Recipe>>
}
