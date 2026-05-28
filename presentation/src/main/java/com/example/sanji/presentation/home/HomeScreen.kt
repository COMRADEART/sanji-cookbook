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

@Composable
fun HomeScreen(onNavigateToSearch: () -> Unit) {
    var isServing by remember { mutableStateOf(false) }
    
    val translationY by animateFloatAsState(
        targetValue = if (isServing) -50f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "servingAnimation"
    )

    val scale by animateFloatAsState(
        targetValue = if (isServing) 1.2f else 1.0f,
        animationSpec = tween(durationMillis = 500),
        label = "scaleAnimation"
    )

    LaunchedEffect(isServing) {
        if (isServing) {
            kotlinx.coroutines.delay(600)
            onNavigateToSearch()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Sanji's Cookbook",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "THE BLACK LEG KITCHEN",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            // Note: In a real app, R.drawable.sanji would be the actual image
            // For now, we use a placeholder icon/image
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .graphicsLayer(
                        translationY = translationY,
                        scaleX = scale,
                        scaleY = scale
                    )
                    .clickable { isServing = true },
                contentAlignment = Alignment.Center
            ) {
                // Image(painter = painterResource(id = R.drawable.sanji_dish), contentDescription = "Sanji serving")
                Text("🍽️", style = MaterialTheme.typography.displayLarge)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "TAP TO START THE SERVICE",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(48.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Chef's Tip:",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        text = "A real cook never wastes a single scrap of food! Respect the ingredients.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }
    }
}
