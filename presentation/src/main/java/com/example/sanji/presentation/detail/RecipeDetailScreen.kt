package com.example.sanji.presentation.detail

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.sanji.presentation.recipe.RecipeViewModel
import kotlinx.coroutines.delay

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
                title = { 
                    Text(
                        recipe?.title?.uppercase() ?: "RECIPE DETAILS",
                        style = MaterialTheme.typography.titleLarge,
                        letterSpacing = 2.sp
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        if (recipe == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Recipe not found.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = recipe.description,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 28.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }

                item {
                    // Nano Banana Living Dish Preview - Refined
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                            .padding(bottom = 32.dp),
                        shape = RoundedCornerShape(32.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                    ) {
                        Box(contentAlignment = androidx.compose.ui.Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            if (livingDishStatus == "processing") {
                                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.secondary,
                                        strokeWidth = 3.dp,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(
                                        text = "SANJI IS PREPARING THE MASTERPIECE...", 
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.secondary,
                                        letterSpacing = 2.sp
                                    )
                                }
                            } else {
                                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                                    Text(
                                        text = "NANO BANANA CINEMATICS", 
                                        style = MaterialTheme.typography.labelSmall, 
                                        color = MaterialTheme.colorScheme.secondary,
                                        letterSpacing = 3.sp
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(
                                        text = if (livingDishStatus == "completed") "🥘✨" else "🍽️", 
                                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 110.sp)
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(
                                        text = "DYNAMIC MOTION: ORBITAL REVEAL", 
                                        style = MaterialTheme.typography.labelSmall, 
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                        letterSpacing = 1.sp
                                    )
                                }
                            }
                        }
                    }
                }
                
                item {
                    Text(
                        text = "THE INGREDIENTS",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                
                itemsIndexed(recipe.ingredients) { index, ingredient ->
                    var isChecked by remember { mutableStateOf(false) }
                    
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        delay(index * 50L)
                        visible = true
                    }

                    AnimatedVisibility(
                        visible = visible,
                        enter = slideInHorizontally { it / 2 } + fadeIn()
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { isChecked = !isChecked },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isChecked) MaterialTheme.colorScheme.surface else Color.Transparent,
                            border = if (isChecked) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)) else null
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isChecked, 
                                    onCheckedChange = { isChecked = it },
                                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.secondary)
                                )
                                Text(
                                    text = ingredient,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.weight(1f).padding(start = 12.dp),
                                    textDecoration = if (isChecked) androidx.compose.ui.text.style.TextDecoration.LineThrough else null,
                                    color = if (isChecked) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f) else MaterialTheme.colorScheme.onSurface
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
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(48.dp))
                    // Diable Jambe Red Button - Consistent with Home
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .clickable { recipe?.id?.let { onStartCooking(it) } }
                            .graphicsLayer {
                                shadowElevation = 12.dp.toPx()
                                shape = RoundedCornerShape(9999.dp)
                                clip = true
                                ambientShadowColor = Color(0xFFB22222)
                                spotShadowColor = Color(0xFFFF4500)
                            },
                        shape = RoundedCornerShape(9999.dp),
                        color = Color.Transparent
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Color(0xFFB22222), Color(0xFFFF4500))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "COMMENCE THE SERVICE",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                letterSpacing = 3.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(48.dp))
                }
                
                item {
                    Text(
                        text = "CHEF'S INSTRUCTIONS",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }
            }
        }
    }
}
