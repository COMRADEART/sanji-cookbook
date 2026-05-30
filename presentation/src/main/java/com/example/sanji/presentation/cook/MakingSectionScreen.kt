package com.example.sanji.presentation.cook

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sanji.presentation.recipe.RecipeViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakingSectionScreen(
    recipeId: String,
    viewModel: RecipeViewModel,
    onFinish: () -> Unit,
    onBackClick: () -> Unit
) {
    val recipe by produceState<com.example.sanji.domain.model.Recipe?>(initialValue = null) {
        value = viewModel.getRecipeById(recipeId)
    }

    val prepKeywords = listOf("chop", "dice", "mince", "slice", "peel", "cut", "shred", "grate", "prepare", "wash", "rinse")
    val cookingSteps = remember(recipe) {
        recipe?.instructions?.filterNot { step ->
            prepKeywords.any { keyword -> step.contains(keyword, ignoreCase = true) }
        } ?: emptyList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("3. MAKING SECTION", style = MaterialTheme.typography.titleLarge.copy(letterSpacing = 2.sp)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFB22222), // Diable Jambe Red
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Button(
                onClick = onFinish,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB22222))
            ) {
                Text("PLATE THE MASTERPIECE", style = MaterialTheme.typography.titleMedium)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFB22222).copy(alpha = 0.05f), MaterialTheme.colorScheme.background)
                    )
                )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFB22222))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "The stove is a battlefield. Don't let your passion burn the dish.",
                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                itemsIndexed(cookingSteps) { index, step ->
                    CookingStepItem(index + 1, step)
                }
            }
        }
    }
}

@Composable
fun CookingStepItem(index: Int, text: String) {
    val timeInMinutes = remember(text) {
        val regex = "(\\d+)\\s*(minute|minutes|min|mins)".toRegex(RegexOption.IGNORE_CASE)
        regex.find(text)?.groupValues?.get(1)?.toIntOrNull()
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = Color(0xFFB22222),
                    shape = androidx.compose.foundation.shape.CircleShape,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = index.toString(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "STEP $index",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFFB22222),
                    letterSpacing = 1.sp
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge.copy(lineHeight = 32.sp),
                color = MaterialTheme.colorScheme.onSurface
            )

            if (timeInMinutes != null) {
                Spacer(modifier = Modifier.height(20.dp))
                CookingTimer(durationMinutes = timeInMinutes)
            }
        }
    }
}

@Composable
fun CookingTimer(durationMinutes: Int) {
    var timeLeftSeconds by remember { mutableIntStateOf(durationMinutes * 60) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (timeLeftSeconds > 0) {
                delay(1000L)
                timeLeftSeconds--
            }
            isRunning = false
        }
    }

    val minutes = timeLeftSeconds / 60
    val seconds = timeLeftSeconds % 60
    val timeString = "%02d:%02d".format(minutes, seconds)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB22222).copy(alpha = 0.05f), androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Timer, contentDescription = null, tint = Color(0xFFB22222))
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = timeString,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Light),
            color = Color(0xFFB22222),
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = { isRunning = !isRunning },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRunning) MaterialTheme.colorScheme.error else Color(0xFFB22222)
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
        ) {
            Text(if (isRunning) "STOP" else "START TIMER")
        }
    }
}
