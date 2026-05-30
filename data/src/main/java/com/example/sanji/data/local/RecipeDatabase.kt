package com.example.sanji.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sanji.data.local.dao.RecipeDao
import com.example.sanji.data.local.entity.CustomRecipeEntity
import com.example.sanji.data.local.entity.GroceryEntity
import com.example.sanji.data.local.entity.RecipeEntity
import com.example.sanji.data.local.entity.ChefMessageEntity
import com.example.sanji.data.local.entity.UserProfileEntity
import com.example.sanji.data.local.entity.MealPlanEntity

@Database(
    entities = [
        RecipeEntity::class, 
        GroceryEntity::class, 
        CustomRecipeEntity::class,
        ChefMessageEntity::class,
        UserProfileEntity::class,
        MealPlanEntity::class
    ], 
    version = 3
)
abstract class RecipeDatabase : RoomDatabase() {
    abstract val recipeDao: RecipeDao
}
