package com.example.sanji.presentation.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.sanji.presentation.R

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontStyle

@Composable
fun HomeScreen(onNavigateToSearch: () -> Unit) {
    var isServing by remember { mutableStateOf(false) }
    
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
            onNavigateToSearch()
        }
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
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Text(
                text = "SANJI'S COOKBOOK",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 38.sp),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "BARATIE SOUS CHEF • SINCE 1997",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 12.dp, bottom = 48.dp)
            )
            
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .graphicsLayer(
                        translationY = translationY,
                        scaleX = scale,
                        scaleY = scale
                    )
                    .clickable { isServing = true },
                contentAlignment = Alignment.Center
            ) {
                // Baratie Gold Glow
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .background(
                            Brush.radialGradient(
                                listOf(MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f), Color.Transparent)
                            )
                        )
                )
                Text("🍽️", style = MaterialTheme.typography.displayLarge.copy(fontSize = 110.sp))
            }
            
            Spacer(modifier = Modifier.height(64.dp))
            
            // Diable Jambe "Start" Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clickable { isServing = true },
                shape = RoundedCornerShape(9999.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
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
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                        color = Color.White,
                        letterSpacing = 3.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Sanji Black Wisdom Card
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
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "\"Ingredients are sacred. A kitchen is a battlefield. Never, ever waste food.\"",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f), 
                            fontStyle = FontStyle.Italic
                        ),
                        lineHeight = 30.sp
                    )
                }
            }
        }
    }
}
