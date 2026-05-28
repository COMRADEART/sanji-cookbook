package com.example.sanji.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sanji.presentation.recipe.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeId: String,
    viewModel: RecipeViewModel,
    onBackClick: () -> Unit,
    onStartCooking: (String) -> Unit
) {
    val recipes by viewModel.recipes.collectAsState()
    val recipe = recipes.find { it.id == recipeId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipe?.title ?: "Recipe Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (recipe == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Recipe not found.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)
            ) {
                item {
                    Text(
                        text = recipe.description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                item {
                    // Nano Banana Living Dish Preview
                    Card(
                        modifier = Modifier.fillMaxWidth().height(200.dp).padding(bottom = 24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Box(contentAlignment = androidx.compose.ui.Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                                Text("🍌 Nano Banana", style = MaterialTheme.typography.labelSmall)
                                Text("LIVING DISH PREVIEW", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Cinematic Motion: Zoom-In", style = MaterialTheme.typography.bodySmall)
                                // In a real app, this would be a VideoPlayer or AsyncImage for GIF
                                Text("🥘✨ (Animated)", style = MaterialTheme.typography.displayMedium)
                            }
                        }
                    }
                }
                
                item {
                    Text(
                        text = "Ingredients",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                
                items(recipe.ingredients) { ingredient ->
                    var isChecked by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { isChecked = !isChecked },
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Checkbox(checked = isChecked, onCheckedChange = { isChecked = it })
                        Text(
                            text = ingredient,
                            modifier = Modifier.weight(1f),
                            textDecoration = if (isChecked) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                        )
                        IconButton(onClick = { viewModel.addIngredientToGrocery(recipe, ingredient) }) {
                            Icon(androidx.compose.material.icons.filled.Add, contentDescription = "Add to grocery")
                        }
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { recipe?.id?.let { onStartCooking(it) } },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("START COOKING")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                items(recipe.instructions.withIndex().toList()) { (index, instruction) ->
                    Text("${index + 1}. $instruction", modifier = Modifier.padding(bottom = 8.dp))
                }
                
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Preparation Time: ${recipe.prepTime}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
