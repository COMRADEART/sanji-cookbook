package com.example.sanji.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grocery_items")
data class GroceryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val recipeId: String?,
    val isBought: Boolean
)
