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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
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
    val livingDishStatus by viewModel.livingDishStatus.collectAsState()
    val livingDishUrl by viewModel.livingDishUrl.collectAsState()

    // Trigger generation on launch if not already completed
    LaunchedEffect(recipeId) {
        if (recipe != null && livingDishStatus == null) {
            viewModel.generateLivingDish(recipe.title)
        }
    }

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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                            .padding(bottom = 32.dp),
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
                    ) {
                        Box(contentAlignment = androidx.compose.ui.Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            if (livingDishStatus == "processing") {
                                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "SANJI IS PREPARING THE MASTERPIECE...", 
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            } else {
                                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                                    Text(
                                        text = "🍌 NANO BANANA CINEMATICS", 
                                        style = MaterialTheme.typography.labelSmall, 
                                        color = MaterialTheme.colorScheme.primary,
                                        letterSpacing = 3.sp
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    // In a real app, this would be a VideoPlayer or AsyncImage for GIF
                                    Text(
                                        text = if (livingDishStatus == "completed") "🥘✨" else "🍽️", 
                                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 90.sp)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "DYNAMIC MOTION: ORBITAL REVEAL", 
                                        style = MaterialTheme.typography.labelSmall, 
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }
                    }
                }
                
                item {
                    Text(
                        text = "THE INGREDIENTS",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                
                items(recipe.ingredients) { ingredient ->
                    var isChecked by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isChecked = !isChecked }
                            .padding(vertical = 8.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked, 
                            onCheckedChange = { isChecked = it },
                            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                        )
                        Text(
                            text = ingredient,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f).padding(start = 12.dp),
                            textDecoration = if (isChecked) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                        )
                        IconButton(onClick = { viewModel.addIngredientToGrocery(recipe, ingredient) }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add to grocery",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(48.dp))
                    // Diable Jambe Red Button
                    Button(
                        onClick = { recipe?.id?.let { onStartCooking(it) } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = RoundedCornerShape(9999.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB22222)),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        Text(
                            text = "COMMENCE THE SERVICE", 
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp), 
                            color = Color.White,
                            letterSpacing = 2.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(48.dp))
                }
                
                item {
                    Text(
                        text = "CHEF'S INSTRUCTIONS",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
        }
    }
}
