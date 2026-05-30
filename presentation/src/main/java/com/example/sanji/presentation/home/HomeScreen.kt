package com.example.sanji.presentation.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.sanji.presentation.R

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontStyle

@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onStartDirectCooking: (String) -> Unit
) {
    var isServing by remember { mutableStateOf(false) }
    var showActionDialog by remember { mutableStateOf(false) }
    
    // Staggered Entrance State
    var animateEntrance by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        animateEntrance = true
    }

    val translationY by animateFloatAsState(
        targetValue = if (isServing) -120f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "servingAnimation"
    )

    val scale by animateFloatAsState(
        targetValue = if (isServing) 1.6f else 1.0f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "scaleAnimation"
    )

    LaunchedEffect(isServing) {
        if (isServing) {
            kotlinx.coroutines.delay(1200)
            isServing = false
            showActionDialog = true
        }
    }

    if (showActionDialog) {
        AlertDialog(
            onDismissRequest = { showActionDialog = false },
            containerColor = MaterialTheme.colorScheme.surface,
            title = { 
                Text(
                    "CHEF'S ORDERS", 
                    style = MaterialTheme.typography.titleMedium.copy(letterSpacing = 2.sp),
                    color = MaterialTheme.colorScheme.primary
                ) 
            },
            text = { 
                Text(
                    "The kitchen is ready. What's our next move?",
                    style = MaterialTheme.typography.bodyLarge
                ) 
            },
            confirmButton = {
                Button(
                    onClick = { 
                        showActionDialog = false
                        onNavigateToSearch() 
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("FIND A RECIPE")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { 
                        showActionDialog = false
                        // For now, default to the first featured recipe (e.g. Soba Noodles)
                        onStartDirectCooking("3") 
                    },
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
                ) {
                    Text("START COOKING", color = MaterialTheme.colorScheme.secondary)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            // Title & Subtitle with Staggered Entrance
            AnimatedVisibility(
                visible = animateEntrance,
                enter = fadeIn(tween(800)) + slideInVertically(initialOffsetY = { -20 }, animationSpec = tween(800))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "SANJI'S COOKBOOK",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                    
                    Text(
                        text = "BARATIE SOUS CHEF • SINCE 1997",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))

            // Main Image with Staggered Entrance
            AnimatedVisibility(
                visible = animateEntrance,
                enter = fadeIn(tween(1000, 300)) + scaleIn(initialScale = 0.9f, animationSpec = tween(1000, 300))
            ) {
                Box(
                    modifier = Modifier
                        .size(360.dp)
                        .graphicsLayer(
                            translationY = translationY,
                            scaleX = scale,
                            scaleY = scale
                        )
                        .shadow(24.dp, RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp))
                        .clickable { isServing = true },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sanji_baratie),
                        contentDescription = "Chef Sanji serving a masterpiece",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Diable Jambe "Start" Button with Staggered Entrance & Glow
            AnimatedVisibility(
                visible = animateEntrance,
                enter = fadeIn(tween(800, 600)) + slideInVertically(initialOffsetY = { 20 }, animationSpec = tween(800, 600))
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clickable { isServing = true }
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
            }

            Spacer(modifier = Modifier.height(56.dp))

            // Sanji Black Wisdom Card with Staggered Entrance
            AnimatedVisibility(
                visible = animateEntrance,
                enter = fadeIn(tween(800, 900)) + slideInVertically(initialOffsetY = { 40 }, animationSpec = tween(800, 900))
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(28.dp)) {
                        Text(
                            text = "CHEF'S MANDATE",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "\"Ingredients are sacred. A kitchen is a battlefield. Never, ever waste food.\"",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f), 
                                fontStyle = FontStyle.Italic
                            )
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}
