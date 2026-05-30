package com.example.sanji.presentation.chef

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sanji.domain.model.ChefMessage
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChefCompanionScreen(
    viewModel: ChefViewModel,
    onNavigateToMealPlan: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val context = LocalContext.current

    // Automatically scroll to bottom when new messages arrive
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    // Image Picker Launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val file = File(context.cacheDir, "ingredient_temp_${System.currentTimeMillis()}.jpg")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                viewModel.uploadIngredientsImage(file)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.02f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Pinned Status & Trust Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Small Emotional Avatar
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.sweepGradient(
                                        colors = when (state.emotionalState) {
                                            "Mellorine" -> listOf(Color(0xFFFFB6C1), Color(0xFFFF69B4), Color(0xFFFFB6C1))
                                            "Focused" -> listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                                            "Strict" -> listOf(MaterialTheme.colorScheme.error, Color.Black, MaterialTheme.colorScheme.error)
                                            else -> listOf(MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.tertiary)
                                        }
                                    )
                                )
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (state.emotionalState) {
                                    "Mellorine" -> "😍"
                                    "Focused" -> "👨‍🍳"
                                    "Strict" -> "😠"
                                    else -> "🔥"
                                },
                                fontSize = 24.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Chef Sanji",
                                style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "${state.emotionalState} State",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        // TTS Toggle Button
                        IconButton(
                            onClick = { viewModel.toggleTts(!state.isTtsEnabled) }
                        ) {
                            Icon(
                                imageVector = if (state.isTtsEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                                contentDescription = "TTS Switch",
                                tint = if (state.isTtsEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        }

                        // Meal Planner Shortcut
                        IconButton(
                            onClick = onNavigateToMealPlan
                        ) {
                            Icon(
                                imageVector = Icons.Default.RestaurantMenu,
                                contentDescription = "Meal Plan",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }

                        // Clear Chat Button
                        IconButton(
                            onClick = { viewModel.clearChatHistory() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Clear History",
                                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Rapport / Trust Indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Rapport: ",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        LinearProgressIndicator(
                            progress = state.trustLevel / 100f,
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${state.trustLevel}%",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            // Message Stream Area
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (state.messages.isEmpty()) {
                    item {
                        var visible by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) { visible = true }

                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(initialOffsetY = { it / 2 })
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp, horizontal = 8.dp),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                border = BorderStroke(1.dp, Brush.linearGradient(listOf(MaterialTheme.colorScheme.secondary, Color.Transparent)))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f), Color.Transparent)
                                            )
                                        )
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "👨‍🍳",
                                        fontSize = 48.sp,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )
                                    Text(
                                        text = "Baratie Welcome",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.secondary,
                                        letterSpacing = 2.sp
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = state.response,
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontWeight = FontWeight.Light,
                                            fontStyle = FontStyle.Italic,
                                            lineHeight = 32.sp
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(
                                        text = "Snap ingredients, plan meals, or just ask for a masterpiece.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                } else {
                    items(state.messages) { message ->
                        val isChef = message.sender == "chef"
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (isChef) Arrangement.Start else Arrangement.End
                        ) {
                            Card(
                                shape = RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomEnd = if (isChef) 16.dp else 4.dp,
                                    bottomStart = if (isChef) 4.dp else 16.dp
                                ),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isChef) {
                                        MaterialTheme.colorScheme.surfaceVariant
                                    } else {
                                        MaterialTheme.colorScheme.primary
                                    }
                                ),
                                modifier = Modifier.padding(
                                    start = if (isChef) 0.dp else 40.dp,
                                    end = if (isChef) 40.dp else 0.dp
                                )
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = message.text,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                        color = if (isChef) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary
                                    )
                                    
                                    // If this is a chef response and has chef tips, show them
                                    if (isChef && message.text == state.response && state.chefTips.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f))
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "CHEF'S TIPS:",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                        state.chefTips.forEach { tip ->
                                            Text(
                                                text = "• $tip",
                                                style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // If processing, show loading indicator in message thread
                if (state.isProcessing) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Card(
                                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.padding(end = 40.dp)
                            ) {
                                Box(modifier = Modifier.padding(12.dp)) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }
                    }
                }

                // Recognized Ingredients panel inside chat flow
                if (state.recognizedIngredients.isNotEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.9f)),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Ingredients Identified",
                                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                                        color = MaterialTheme.colorScheme.tertiary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                
                                if (state.visionSuggestion.isNotEmpty()) {
                                    Text(
                                        text = state.visionSuggestion,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    state.recognizedIngredients.forEach { ingredient ->
                                        SuggestionChip(
                                            onClick = {},
                                            label = { Text(ingredient) }
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = { viewModel.generateRecipeFromRecognized() },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                                    ) {
                                        Text("Generate Recipe")
                                    }
                                }
                            }
                        }
                    }
                }

                // Generated custom recipe display inside chat flow
                if (state.generatedRecipe != null) {
                    val recipe = state.generatedRecipe!!
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = recipe.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = recipe.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    Text(
                                        text = "🕒 Prep: ${recipe.prepTime}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(
                                        text = "📂 Category: ${recipe.category}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Ingredients:",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                recipe.ingredients.forEach { ing ->
                                    Text(text = "• $ing", style = MaterialTheme.typography.bodySmall)
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Instructions:",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                recipe.instructions.forEachIndexed { index, step ->
                                    Text(text = "${index + 1}. $step", style = MaterialTheme.typography.bodySmall)
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { viewModel.saveGeneratedRecipe() },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                                ) {
                                    Icon(Icons.Default.Save, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Save to Cookbook")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom Control and Input Panel
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Photo Attachment Button
                IconButton(
                    onClick = { galleryLauncher.launch("image/*") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Photo,
                        contentDescription = "Upload Photo",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                // Text/Chat Input Field
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Talk to the chef...") },
                    maxLines = 3,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (messageText.isNotBlank()) {
                                    viewModel.onSendMessage(messageText)
                                    messageText = ""
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    shape = RoundedCornerShape(24.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                // Microphone Input Button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (state.isRecording) {
                                Color.Red.copy(alpha = 0.2f)
                            } else {
                                Color.Transparent
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            if (state.isRecording) {
                                viewModel.stopVoiceInteraction()
                            } else {
                                viewModel.startVoiceInteraction()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice Input",
                            tint = if (state.isRecording) Color.Red else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
