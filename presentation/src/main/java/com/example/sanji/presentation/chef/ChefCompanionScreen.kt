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

import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChefCompanionScreen(viewModel: ChefViewModel) {
    val state by viewModel.state.collectAsState()
    var messageText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.03f)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Text(
                text = "CHEF SANJI",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "SOUS CHEF OF THE BARATIE • STANDING BY",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            // Premium Emotional Avatar
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.sweepGradient(
                            colors = when (state.emotionalState) {
                                "Mellorine" -> listOf(Color(0xFFFFB6C1), Color(0xFFFF69B4), Color(0xFFFFB6C1))
                                "Focused" -> listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.primary)
                                "Strict" -> listOf(MaterialTheme.colorScheme.tertiary, Color.Black, MaterialTheme.colorScheme.tertiary)
                                else -> listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                            }
                        )
                    )
                    .padding(4.dp) // Border thickness
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
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
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 64.sp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Glassmorphism Response Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
            ) {
                LazyColumn(modifier = Modifier.padding(28.dp)) {
                    item {
                        Text(
                            text = state.response,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 32.sp
                        )
                    }
                    
                    if (state.chefTips.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "CHEF'S WISDOM", 
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            state.chefTips.forEach { tip ->
                                Text(
                                    text = "• $tip", 
                                    style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
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
