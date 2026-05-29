package com.example.sanji.data.repository

import com.example.sanji.data.local.dao.RecipeDao
import com.example.sanji.data.local.entity.RecipeEntity
import com.example.sanji.data.local.entity.GroceryEntity
import com.example.sanji.data.local.entity.CustomRecipeEntity
import com.example.sanji.data.mapper.toDomain
import com.example.sanji.data.remote.dto.RecipeDto
import com.example.sanji.domain.model.Recipe
import com.example.sanji.domain.model.GroceryItem
import com.example.sanji.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import com.example.sanji.core.resilience.CircuitBreaker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecipeRepositoryImpl(
    private val dao: RecipeDao
) : RecipeRepository {

    private val circuitBreaker = CircuitBreaker()
    private val _isCloudAvailable = MutableStateFlow(true)

    private val mockRecipes = listOf(
        RecipeDto(
            id = "1",
            title = "Macedoine of Apple",
            description = "A refreshing apple salad served at the Baratie.",
            ingredients = listOf("Apples", "Honey", "Mint"),
            instructions = listOf("Peel and dice apples.", "Mix with honey.", "Garnish with mint."),
            imageUrl = "https://example.com/apple.jpg",
            prepTime = "15 mins",
            category = "Appetizer",
            tags = listOf("Healthy", "Fruit"),
            chefTips = listOf("Use crisp apples for better texture.")
        ),
        RecipeDto(
            id = "2",
            title = "Baratie's Soup",
            description = "The famous seafood soup from the Baratie restaurant.",
            ingredients = listOf("Fish", "Shrimp", "Clams", "Tomatoes", "Onions"),
            instructions = listOf("Sauté onions.", "Add tomatoes and water.", "Add seafood and simmer."),
            imageUrl = "https://example.com/soup.jpg",
            prepTime = "45 mins",
            category = "Seafood",
            tags = listOf("Main Course", "Classic"),
            chefTips = listOf("Don't overcook the shrimp!")
        ),
        RecipeDto(
            id = "3",
            title = "Soba Noodles",
            description = "Sanji's special soba noodles prepared during the Wano arc.",
            ingredients = listOf("Buckwheat noodles", "Dashi", "Scallions", "Wasabi"),
            instructions = listOf("Boil noodles.", "Prepare dashi broth.", "Serve cold with scallions and wasabi."),
            imageUrl = "https://example.com/soba.jpg",
            prepTime = "20 mins",
            category = "Noodles",
            tags = listOf("Wano", "Vegetarian"),
            chefTips = listOf("Rinse noodles in cold water immediately after boiling.")
        )
    )

    override suspend fun getRecipes(): List<Recipe> {
        return mockRecipes.map { dto ->
            val entity = dao.getRecipeById(dto.id)
            dto.toDomain(entity?.isFavorite ?: false)
        }
    }

    override suspend fun getRecipeById(id: String): Recipe? {
        val dto = mockRecipes.find { it.id == id } ?: return null
        val entity = dao.getRecipeById(id)
        return dto.toDomain(entity?.isFavorite ?: false)
    }

    override suspend fun toggleFavorite(recipeId: String) {
        val entity = dao.getRecipeById(recipeId)
        if (entity == null) {
            dao.insertRecipe(RecipeEntity(recipeId, true))
        } else {
            dao.updateFavoriteStatus(recipeId, !entity.isFavorite)
        }
    }

    override fun observeFavorites(): Flow<List<Recipe>> {
        return dao.observeFavorites().map { entities ->
            val favoriteIds = entities.map { it.id }
            mockRecipes.filter { it.id in favoriteIds }.map { it.toDomain(true) }
        }
    }

    override suspend fun searchRecipes(query: String, category: String?): List<Recipe> {
        return mockRecipes.filter { 
            (it.title.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true)) &&
            (category == null || it.category == category)
        }.map { dto ->
            val entity = dao.getRecipeById(dto.id)
            dto.toDomain(entity?.isFavorite ?: false)
        }
    }

    override fun observeGroceryList(): Flow<List<GroceryItem>> {
        return dao.observeGroceryList().map { entities ->
            entities.map { GroceryItem(it.id, it.name, it.recipeId, it.isBought) }
        }
    }

    override suspend fun addGroceryItem(item: GroceryItem) {
        dao.insertGroceryItem(GroceryEntity(item.id, item.name, item.recipeId, item.isBought))
    }

    override suspend fun toggleGroceryItem(id: String) {
        dao.toggleGroceryItem(id)
    }

    override suspend fun removeGroceryItem(id: String) {
        dao.deleteGroceryItem(id)
    }

    override suspend fun clearBoughtItems() {
        dao.clearBoughtItems()
    }

    override suspend fun addCustomRecipe(recipe: Recipe) {
        dao.insertCustomRecipe(CustomRecipeEntity(
            id = recipe.id,
            title = recipe.title,
            description = recipe.description,
            ingredients = recipe.ingredients.joinToString(","),
            instructions = recipe.instructions.joinToString("|"),
            imageUrl = recipe.imageUrl,
            prepTime = recipe.prepTime,
            category = recipe.category
        ))
    }

    override fun observeCustomRecipes(): Flow<List<Recipe>> {
        return dao.observeCustomRecipes().map { entities ->
            entities.map {
                Recipe(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    ingredients = it.ingredients.split(","),
                    instructions = it.instructions.split("|"),
                    imageUrl = it.imageUrl,
                    prepTime = it.prepTime,
                    category = it.category,
                    isCustom = true
                )
            }
        }
    }

    override fun observeCloudStatus(): Flow<Boolean> = _isCloudAvailable.asStateFlow()

    // Example of a resilient method using Circuit Breaker
    suspend fun getAiChefResponse(message: String): String {
        return circuitBreaker.execute(
            fallback = { 
                _isCloudAvailable.value = false
                "I'm a bit tied up in the kitchen right now! (Offline Mode)" 
            },
            action = {
                // Actual API logic would go here
                _isCloudAvailable.value = true
                "A masterful choice!"
            }
        )
    }
}
