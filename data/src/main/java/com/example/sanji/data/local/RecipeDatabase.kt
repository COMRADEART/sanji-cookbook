package com.example.sanji.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sanji.data.local.dao.RecipeDao
import com.example.sanji.data.local.entity.CustomRecipeEntity
import com.example.sanji.data.local.entity.GroceryEntity
import com.example.sanji.data.local.entity.RecipeEntity

@Database(entities = [RecipeEntity::class, GroceryEntity::class, CustomRecipeEntity::class], version = 2)
abstract class RecipeDatabase : RoomDatabase() {
    abstract val recipeDao: RecipeDao
}
