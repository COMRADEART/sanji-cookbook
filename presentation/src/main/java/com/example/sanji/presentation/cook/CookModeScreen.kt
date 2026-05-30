package com.example.sanji.presentation.cook

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sanji.presentation.recipe.RecipeViewModel

enum class CookStage(val title: String, val color: Color) {
    PREP("PREP AREA", Color(0xFFC5A059)),      // Baratie Gold
    CUTTING("CUTTING BOARD", Color(0xFF1A1A1B)), // Sanji Black
    STOVE("THE STOVE", Color(0xFFB22222))      // Diable Jambe Red
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookModeScreen(
    recipeId: String,
    viewModel: RecipeViewModel,
    onBackClick: () -> Unit,
    onFinish: () -> Unit
) {
    var currentStage by remember { mutableStateOf(CookStage.PREP) }
    
    val recipe by produceState<com.example.sanji.domain.model.Recipe?>(initialValue = null) {
        value = viewModel.getRecipeById(recipeId)
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = recipe?.title?.uppercase() ?: "LOADING...",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                                letterSpacing = 2.sp
                            )
                            Text(
                                text = currentStage.title,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
                StageProgressBar(currentStage = currentStage)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            AnimatedContent(
                targetState = currentStage,
                transitionSpec = {
                    if (targetState.ordinal > initialState.ordinal) {
                        slideInHorizontally { it } + fadeIn() togetherWith
                                slideOutHorizontally { -it } + fadeOut()
                    } else {
                        slideInHorizontally { -it } + fadeIn() togetherWith
                                slideOutHorizontally { it } + fadeOut()
                    }.using(SizeTransform(clip = false))
                },
                label = "StageTransition"
            ) { stage ->
                when (stage) {
                    CookStage.PREP -> PrepAreaContent(
                        recipe = recipe,
                        onNext = { currentStage = CookStage.CUTTING }
                    )
                    CookStage.CUTTING -> CuttingAreaContent(
                        recipe = recipe,
                        onNext = { currentStage = CookStage.STOVE },
                        onBack = { currentStage = CookStage.PREP }
                    )
                    CookStage.STOVE -> MakingSectionContent(
                        recipe = recipe,
                        onFinish = onFinish,
                        onBack = { currentStage = CookStage.CUTTING }
                    )
                }
            }
        }
    }
}

@Composable
fun StageProgressBar(currentStage: CookStage) {
    val animatedProgress by animateFloatAsState(
        targetValue = when (currentStage) {
            CookStage.PREP -> 0.33f
            CookStage.CUTTING -> 0.66f
            CookStage.STOVE -> 1.0f
        },
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "Progress"
    )

    Canvas(modifier = Modifier.fillMaxWidth().height(4.dp)) {
        val width = size.width
        // Background track
        drawLine(
            color = Color.White.copy(alpha = 0.1f),
            start = Offset(0f, 0f),
            end = Offset(width, 0f),
            strokeWidth = size.height,
            cap = StrokeCap.Round
        )
        // Progress track
        drawLine(
            brush = Brush.horizontalGradient(
                colors = listOf(Color(0xFFC5A059), Color(0xFFB22222))
            ),
            start = Offset(0f, 0f),
            end = Offset(width * animatedProgress, 0f),
            strokeWidth = size.height,
            cap = StrokeCap.Round
        )
    }
}
