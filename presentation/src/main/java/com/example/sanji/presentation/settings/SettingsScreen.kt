package com.example.sanji.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        LazyColumn {
            item {
                ListItem(
                    headlineContent = { Text("Dark Mode") },
                    trailingContent = {
                        var checked by remember { mutableStateOf(false) }
                        Switch(checked = checked, onCheckedChange = { checked = it })
                    }
                )
            }
            item { HorizontalDivider() }
            item {
                ListItem(
                    headlineContent = { Text("Profile") },
                    leadingContent = { Icon(Icons.Default.Person, contentDescription = null) }
                )
            }
            item {
                ListItem(
                    headlineContent = { Text("Notifications") },
                    leadingContent = { Icon(Icons.Default.Notifications, contentDescription = null) }
                )
            }
            item { HorizontalDivider() }
            item {
                ListItem(
                    headlineContent = { Text("About Sanji's Cookbook") },
                    leadingContent = { Icon(Icons.Default.Info, contentDescription = null) },
                    supportingContent = { Text("Version 1.0.0") }
                )
            }
        }
    }
}
