package com.example.sanji.presentation.chef

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sanji.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChefCompanionScreen(viewModel: ChefViewModel) {
    val state by viewModel.state.collectAsState()
    var messageText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Chef Sanji",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // Emotional Avatar Placeholder
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(
                    when (state.emotionalState) {
                        "Mellorine" -> Color(0xFFFFB6C1)
                        "Focused" -> Color(0xFFADD8E6)
                        "Strict" -> Color(0xFFFF4500)
                        else -> MaterialTheme.colorScheme.primaryContainer
                    }
                )
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when (state.emotionalState) {
                    "Mellorine" -> "😍"
                    "Focused" -> "👨‍🍳"
                    "Strict" -> "😠"
                    else -> "🔥"
                },
                style = MaterialTheme.typography.displayLarge
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sanji's Response Bubble
        Card(
            modifier = Modifier.fillMaxWidth().weight(1f),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    Text(
                        text = state.response,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                if (state.chefTips.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Chef's Tips:", style = MaterialTheme.typography.labelLarge)
                        state.chefTips.forEach { tip ->
                            Text(text = "• $tip", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Interaction Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { 
                    if (state.isRecording) viewModel.stopVoiceInteraction() 
                    else viewModel.startVoiceInteraction() 
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice",
                    tint = if (state.isRecording) Color.Red else MaterialTheme.colorScheme.primary
                )
            }
            
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Talk to the chef...") },
                trailingIcon = {
                    IconButton(onClick = { 
                        viewModel.onSendMessage(messageText)
                        messageText = ""
                    }) {
                        Icon(Icons.Default.Send, contentDescription = "Send")
                    }
                }
            )
        }
    }
}
