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
import com.example.sanji.presentation.R

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign

@Composable
fun HomeScreen(onNavigateToSearch: () -> Unit) {
    var isServing by remember { mutableStateOf(false) }
    
    val translationY by animateFloatAsState(
        targetValue = if (isServing) -100f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "servingAnimation"
    )

    val scale by animateFloatAsState(
        targetValue = if (isServing) 1.5f else 1.0f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "scaleAnimation"
    )

    LaunchedEffect(isServing) {
        if (isServing) {
            kotlinx.coroutines.delay(1000)
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
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
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
                text = "Sanji's Cookbook",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "THE BLACK LEG KITCHEN • EST. 1997",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 8.dp, bottom = 48.dp)
            )
            
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .graphicsLayer(
                        translationY = translationY,
                        scaleX = scale,
                        scaleY = scale
                    )
                    .clickable { isServing = true },
                contentAlignment = Alignment.Center
            ) {
                // Background Glow
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(
                            Brush.radialGradient(
                                listOf(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f), Color.Transparent)
                            )
                        )
                )
                Text("🍽️", style = MaterialTheme.typography.displayLarge.copy(fontSize = 100.sp))
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = "TAP TO COMMENCE THE SERVICE",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(64.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "CHEF'S WISDOM",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "\"A real cook never wastes a single scrap of food! Respect the ingredients with your life.\"",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                        lineHeight = 28.sp
                    )
                }
            }
        }
    }
}
