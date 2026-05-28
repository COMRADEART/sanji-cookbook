package com.example.sanji.data.mapper

import com.example.sanji.data.remote.dto.RecipeDto
import com.example.sanji.domain.model.Recipe

fun RecipeDto.toDomain(isFavorite: Boolean): Recipe {
    return Recipe(
        id = id,
        title = title,
        description = description,
        ingredients = ingredients,
        instructions = instructions,
        imageUrl = imageUrl,
        prepTime = prepTime,
        category = category,
        tags = tags,
        chefTips = chefTips,
        isFavorite = isFavorite,
        isCustom = false
    )
}
