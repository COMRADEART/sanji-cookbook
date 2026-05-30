package com.example.sanji.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Favorites : Screen("favorites")
    object GroceryList : Screen("grocery_list")
    object ChefCompanion : Screen("chef_companion")
    object Settings : Screen("settings")
    object MealPlan : Screen("meal_plan")
    object RecipeDetail : Screen("recipe_detail/{recipeId}") {
        fun createRoute(recipeId: String) = "recipe_detail/$recipeId"
    }
    object CookMode : Screen("cook_mode/{recipeId}") {
        fun createRoute(recipeId: String) = "cook_mode/$recipeId"
    }
    object PrepArea : Screen("prep_area/{recipeId}") {
        fun createRoute(recipeId: String) = "prep_area/$recipeId"
    }
    object CuttingArea : Screen("cutting_area/{recipeId}") {
        fun createRoute(recipeId: String) = "cutting_area/$recipeId"
    }
    object MakingSection : Screen("making_section/{recipeId}") {
        fun createRoute(recipeId: String) = "making_section/$recipeId"
    }
}
