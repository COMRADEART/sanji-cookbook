package com.example.sanji.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.sanji.presentation.detail.RecipeDetailScreen
import com.example.sanji.presentation.favorites.FavoritesScreen
import com.example.sanji.presentation.home.HomeScreen
import com.example.sanji.presentation.navigation.Screen
import com.example.sanji.presentation.recipe.RecipeViewModel
import com.example.sanji.presentation.search.SearchScreen
import com.example.sanji.presentation.settings.SettingsScreen
import dagger.hilt.android.AndroidEntryPoint

import com.example.sanji.presentation.theme.BaratieTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaratieTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel: RecipeViewModel = hiltViewModel()
    val isCloudAvailable by viewModel.isCloudAvailable.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = listOf(
        Triple(Screen.Home, "Home", Icons.Default.Home),
        Triple(Screen.Search, "Search", Icons.Default.Search),
        Triple(Screen.ChefCompanion, "Chef", Icons.Default.Person),
        Triple(Screen.GroceryList, "Grocery", Icons.Default.ShoppingCart),
        Triple(Screen.Favorites, "Favorites", Icons.Default.Favorite),
        Triple(Screen.Settings, "Settings", Icons.Default.Settings)
    )

    Scaffold(
        topBar = {
            if (!isCloudAvailable) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "OFFLINE MODE: AI CHEF IS TIED UP",
                        modifier = Modifier.padding(8.dp).fillMaxWidth(),
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        },
        bottomBar = {
            if (currentDestination?.route != Screen.Home.route && 
                currentDestination?.route?.contains("cook_mode") == false) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    tonalElevation = 8.dp
                ) {
                    items.forEach { (screen, label, icon) ->
                        NavigationBarItem(
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.secondary,
                                selectedTextColor = MaterialTheme.colorScheme.secondary,
                                unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                                unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                                indicatorColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f)
                            ),
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(onNavigateToSearch = {
                    navController.navigate(Screen.Search.route)
                })
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    viewModel = viewModel,
                    onRecipeClick = { recipeId ->
                        navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                    }
                )
            }
            composable(Screen.ChefCompanion.route) {
                val chefViewModel: com.example.sanji.presentation.chef.ChefViewModel = hiltViewModel()
                com.example.sanji.presentation.chef.ChefCompanionScreen(
                    viewModel = chefViewModel,
                    onNavigateToMealPlan = { navController.navigate(Screen.MealPlan.route) }
                )
            }
            composable(Screen.MealPlan.route) {
                val chefViewModel: com.example.sanji.presentation.chef.ChefViewModel = hiltViewModel()
                com.example.sanji.presentation.mealplan.MealPlanScreen(
                    viewModel = chefViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    viewModel = viewModel,
                    onRecipeClick = { recipeId ->
                        navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                    }
                )
            }
            composable(Screen.GroceryList.route) {
                com.example.sanji.presentation.grocery.GroceryListScreen(viewModel = viewModel)
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
            composable(Screen.RecipeDetail.route) { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
                RecipeDetailScreen(
                    recipeId = recipeId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onStartCooking = { id ->
                        navController.navigate(Screen.CookMode.createRoute(id))
                    }
                )
            }
            composable(Screen.CookMode.route) { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
                com.example.sanji.presentation.cook.CookModeScreen(
                    recipeId = recipeId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
