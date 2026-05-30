package com.example.sanji.data.local.dao

import androidx.room.*
import com.example.sanji.data.local.entity.RecipeEntity
import com.example.sanji.data.local.entity.GroceryEntity
import com.example.sanji.data.local.entity.CustomRecipeEntity
import com.example.sanji.data.local.entity.ChefMessageEntity
import com.example.sanji.data.local.entity.UserProfileEntity
import com.example.sanji.data.local.entity.MealPlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes WHERE isFavorite = 1")
    fun observeFavorites(): Flow<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeById(id: String): RecipeEntity?

    @Query("UPDATE recipes SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean)

    // Grocery Items
    @Query("SELECT * FROM grocery_items")
    fun observeGroceryList(): Flow<List<GroceryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroceryItem(item: GroceryEntity)

    @Query("UPDATE grocery_items SET isBought = NOT isBought WHERE id = :id")
    suspend fun toggleGroceryItem(id: String)

    @Query("DELETE FROM grocery_items WHERE id = :id")
    suspend fun deleteGroceryItem(id: String)

    @Query("DELETE FROM grocery_items WHERE isBought = 1")
    suspend fun clearBoughtItems()

    // Custom Recipes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomRecipe(recipe: CustomRecipeEntity)

    @Query("SELECT * FROM custom_recipes")
    fun observeCustomRecipes(): Flow<List<CustomRecipeEntity>>

    // Chef Message History
    @Query("SELECT * FROM chef_messages ORDER BY timestamp ASC")
    fun observeMessages(): Flow<List<ChefMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChefMessageEntity)

    @Query("DELETE FROM chef_messages")
    suspend fun clearMessages()

    // User Profile
    @Query("SELECT * FROM user_profile WHERE userId = :userId")
    suspend fun getUserProfile(userId: String): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: UserProfileEntity)

    // Meal Plan
    @Query("SELECT * FROM meal_plans ORDER BY day ASC, id ASC")
    fun observeMealPlans(): Flow<List<MealPlanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealPlan(mealPlan: MealPlanEntity)

    @Query("DELETE FROM meal_plans")
    suspend fun clearMealPlans()
}
