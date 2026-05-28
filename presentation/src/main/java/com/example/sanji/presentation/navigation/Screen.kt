package com.example.sanji.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Favorites : Screen("favorites")
    object GroceryList : Screen("grocery_list")
    object ChefCompanion : Screen("chef_companion")
    object Settings : Screen("settings")
    object RecipeDetail : Screen("recipe_detail/{recipeId}") {
        fun createRoute(recipeId: String) = "recipe_detail/$recipeId"
    }
    object CookMode : Screen("cook_mode/{recipeId}") {
        fun createRoute(recipeId: String) = "cook_mode/$recipeId"
    }
}
