package com.example.sanji.presentation.cook

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sanji.presentation.recipe.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookModeScreen(
    recipeId: String,
    viewModel: RecipeViewModel,
    onBackClick: () -> Unit
) {
    val recipes by viewModel.recipes.collectAsState()
    val recipe = recipes.find { it.id == recipeId }
    val timerSeconds by viewModel.timerSeconds.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cooking: ${recipe?.title}") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (recipe == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Recipe not found.")
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
                // Timer Section
                if (timerSeconds > 0) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Timer: ${timerSeconds / 60}:${(timerSeconds % 60).toString().padStart(2, '0')}")
                            Button(onClick = { viewModel.stopTimer() }) {
                                Text("Stop")
                            }
                        }
                    }
                }

                Text(
                    text = "Steps",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn {
                    items(recipe.instructions.withIndex().toList()) { (index, instruction) ->
                        StepItem(
                            index = index + 1,
                            instruction = instruction,
                            onStartTimer = { viewModel.startTimer(300) } // Default 5 mins for now
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StepItem(index: Int, instruction: String, onStartTimer: () -> Unit) {
    var isDone by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDone) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = isDone, onCheckedChange = { isDone = it })
            
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text(text = "Step $index", style = MaterialTheme.typography.labelLarge)
                Text(text = instruction, style = MaterialTheme.typography.bodyLarge)
                
                if (instruction.contains("minutes", ignoreCase = true) || instruction.contains("simmer", ignoreCase = true)) {
                    TextButton(onClick = onStartTimer) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Text("Start Timer")
                    }
                }
            }
        }
    }
}
