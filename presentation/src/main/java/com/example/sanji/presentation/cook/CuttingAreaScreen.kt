package com.example.sanji.presentation.cook

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sanji.presentation.recipe.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuttingAreaScreen(
    recipeId: String,
    viewModel: RecipeViewModel,
    onNext: () -> Unit,
    onBackClick: () -> Unit
) {
    val recipe by produceState<com.example.sanji.domain.model.Recipe?>(initialValue = null) {
        value = viewModel.getRecipeById(recipeId)
    }

    val prepKeywords = listOf("chop", "dice", "mince", "slice", "peel", "cut", "shred", "grate", "prepare", "wash", "rinse")
    val prepTasks = remember(recipe) {
        recipe?.instructions?.filter { step ->
            prepKeywords.any { keyword -> step.contains(keyword, ignoreCase = true) }
        } ?: emptyList()
    }

    val completedTasks = remember { mutableStateListOf<String>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("2. CUTTING AREA", style = MaterialTheme.typography.titleLarge.copy(letterSpacing = 2.sp)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A1B), // Sanji Black
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            val allDone = prepTasks.isEmpty() || prepTasks.all { it in completedTasks }
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                enabled = allDone,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                )
            ) {
                Text("TO THE STOVE", style = MaterialTheme.typography.titleMedium)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1A1A1B).copy(alpha = 0.05f), MaterialTheme.colorScheme.background)
                    )
                )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ContentCut, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Precision is key. A clumsy blade ruins the finest ingredients.",
                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
            }

            if (prepTasks.isEmpty() && recipe != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No preliminary prep required for this dish. Move to the stove!",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(32.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(prepTasks) { task ->
                        val isDone = task in completedTasks
                        Card(
                            onClick = {
                                if (isDone) completedTasks.remove(task)
                                else completedTasks.add(task)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isDone) MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f) 
                                               else MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (isDone) 0.dp else 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(20.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isDone,
                                    onClick = {
                                        if (isDone) completedTasks.remove(task)
                                        else completedTasks.add(task)
                                    },
                                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.secondary)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = task,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = if (isDone) FontWeight.Normal else FontWeight.Medium,
                                        textDecoration = if (isDone) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                                    ),
                                    color = if (isDone) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                            else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
