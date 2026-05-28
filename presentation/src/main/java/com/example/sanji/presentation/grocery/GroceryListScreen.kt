package com.example.sanji.presentation.grocery

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.sanji.presentation.recipe.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryListScreen(viewModel: RecipeViewModel) {
    val groceryItems by viewModel.groceryList.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Grocery List",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        if (groceryItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Your grocery list is empty.")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(groceryItems) { item ->
                    ListItem(
                        headlineContent = { 
                            Text(
                                text = item.name,
                                textDecoration = if (item.isBought) TextDecoration.LineThrough else null
                            )
                        },
                        leadingContent = {
                            Checkbox(
                                checked = item.isBought,
                                onCheckedChange = { viewModel.toggleGroceryItem(item.id) }
                            )
                        },
                        trailingContent = {
                            IconButton(onClick = { viewModel.removeGroceryItem(item.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove")
                            }
                        }
                    )
                }
            }
        }
    }
}
