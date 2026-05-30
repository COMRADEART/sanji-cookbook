package com.example.sanji.presentation.cook

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sanji.presentation.R
import com.example.sanji.domain.model.Recipe
import kotlinx.coroutines.delay

@Composable
fun PrepAreaContent(
    recipe: Recipe?,
    onNext: () -> Unit
) {
    val checkedIngredients = remember { mutableStateListOf<String>() }
    val allChecked = recipe?.ingredients?.all { it in checkedIngredients } == true

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
            Image(
                painter = painterResource(id = R.drawable.sanji_focused),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                        )
                    )
            )
            Text(
                text = "\"Every ingredient is a soldier. Respect them.\"",
                style = MaterialTheme.typography.titleMedium.copy(fontStyle = FontStyle.Italic),
                color = Color.White,
                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
            )
        }

        Text(
            text = "CHECKLIST",
            style = MaterialTheme.typography.labelLarge,
            color = Color(0xFFC5A059),
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            recipe?.ingredients?.let { ingredients ->
                itemsIndexed(ingredients) { index, ingredient ->
                    val isChecked = ingredient in checkedIngredients
                    
                    // Staggered entrance
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        delay(index * 50L)
                        visible = true
                    }

                    AnimatedVisibility(
                        visible = visible,
                        enter = slideInHorizontally() + fadeIn()
                    ) {
                        IngredientCard(
                            name = ingredient,
                            isChecked = isChecked,
                            onToggle = {
                                if (isChecked) checkedIngredients.remove(ingredient)
                                else checkedIngredients.add(ingredient)
                            }
                        )
                    }
                }
            }
        }

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(64.dp)
                .graphicsLayer {
                    alpha = if (allChecked) 1f else 0.5f
                },
            enabled = allChecked,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC5A059)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("PROCEED TO CUTTING BOARD", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        }
    }
}

@Composable
fun IngredientCard(name: String, isChecked: Boolean, onToggle: () -> Unit) {
    val scale by animateFloatAsState(if (isChecked) 0.98f else 1f, label = "Scale")
    val alpha by animateFloatAsState(if (isChecked) 0.6f else 1f, label = "Alpha")

    Card(
        onClick = onToggle,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isChecked) Color(0xFFC5A059).copy(alpha = 0.05f) else Color.White.copy(alpha = 0.05f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isChecked) Color(0xFFC5A059).copy(alpha = 0.5f) else Color.White.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(1.dp, if (isChecked) Color(0xFFC5A059) else Color.White.copy(alpha = 0.3f), CircleShape)
                    .clip(CircleShape)
                    .background(if (isChecked) Color(0xFFC5A059) else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                if (isChecked) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = if (isChecked) TextDecoration.LineThrough else null
                ),
                color = Color.White.copy(alpha = alpha)
            )
        }
    }
}

@Composable
fun CuttingAreaContent(
    recipe: Recipe?,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val prepKeywords = listOf("chop", "dice", "mince", "slice", "peel", "cut", "shred", "grate", "prepare", "wash", "rinse")
    val prepTasks = remember(recipe) {
        recipe?.instructions?.filter { step ->
            prepKeywords.any { keyword -> step.contains(keyword, ignoreCase = true) }
        } ?: emptyList()
    }
    val completedTasks = remember { mutableStateListOf<String>() }
    val allDone = prepTasks.isEmpty() || prepTasks.all { it in completedTasks }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
            Image(
                painter = painterResource(id = R.drawable.sanji_passionate),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                        )
                    )
            )
            Text(
                text = "\"Precision is the difference between a cook and a chef.\"",
                style = MaterialTheme.typography.titleMedium.copy(fontStyle = FontStyle.Italic),
                color = Color.White,
                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
            )
        }

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.ContentCut, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "TASK LIST",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = 0.6f)
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(prepTasks) { index, task ->
                val isDone = task in completedTasks
                
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    delay(index * 100L)
                    visible = true
                }

                AnimatedVisibility(visible = visible, enter = fadeIn() + expandVertically()) {
                    TaskCard(task = task, isDone = isDone) {
                        if (isDone) completedTasks.remove(task)
                        else completedTasks.add(task)
                    }
                }
            }
        }

        Row(modifier = Modifier.padding(16.dp)) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("BACK")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = onNext,
                modifier = Modifier.weight(2f).height(56.dp),
                enabled = allDone,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("TO THE STOVE", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
        }
    }
}

@Composable
fun TaskCard(task: String, isDone: Boolean, onToggle: () -> Unit) {
    val rotation by animateFloatAsState(if (isDone) 0f else 2f, label = "Rotation")

    Card(
        onClick = onToggle,
        modifier = Modifier.fillMaxWidth().graphicsLayer { rotationZ = rotation },
        colors = CardDefaults.cardColors(
            containerColor = if (isDone) Color.White.copy(alpha = 0.05f) else Color.White.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = isDone, onClick = onToggle, colors = RadioButtonDefaults.colors(selectedColor = Color.White))
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = task,
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = if (isDone) TextDecoration.LineThrough else null
                ),
                color = if (isDone) Color.White.copy(alpha = 0.4f) else Color.White
            )
        }
    }
}

@Composable
fun MakingSectionContent(
    recipe: Recipe?,
    onFinish: () -> Unit,
    onBack: () -> Unit
) {
    val prepKeywords = listOf("chop", "dice", "mince", "slice", "peel", "cut", "shred", "grate", "prepare", "wash", "rinse")
    val cookingSteps = remember(recipe) {
        recipe?.instructions?.filterNot { step ->
            prepKeywords.any { keyword -> step.contains(keyword, ignoreCase = true) }
        } ?: emptyList()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
            Image(
                painter = painterResource(id = R.drawable.sanji_night),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color(0xFFB22222).copy(alpha = 0.9f))
                        )
                    )
            )
            Text(
                text = "\"Don't let the fire die out. This is the finale.\"",
                style = MaterialTheme.typography.titleMedium.copy(fontStyle = FontStyle.Italic),
                color = Color.White,
                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            itemsIndexed(cookingSteps) { index, step ->
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    delay(index * 150L)
                    visible = true
                }
                
                // Get a relevant tip from the recipe if it exists
                val tip = recipe?.chefTips?.getOrNull(index % (recipe.chefTips.size.coerceAtLeast(1)))

                AnimatedVisibility(visible = visible, enter = slideInVertically { it } + fadeIn()) {
                    ProCookingStepItem(index + 1, step, tip)
                }
            }
        }

        Row(modifier = Modifier.padding(16.dp)) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFB22222))
            ) {
                Text("BACK")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = onFinish,
                modifier = Modifier.weight(2f).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB22222)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("PLATE THE MASTERPIECE", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
        }
    }
}

@Composable
fun ProCookingStepItem(index: Int, text: String, tip: String? = null) {
    val timeInMinutes = remember(text) {
        val regex = "(\\d+)\\s*(minute|minutes|min|mins)".toRegex(RegexOption.IGNORE_CASE)
        regex.find(text)?.groupValues?.get(1)?.toIntOrNull()
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = Color(0xFFB22222),
                    shape = CircleShape,
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = index.toString(), color = Color.White, fontWeight = FontWeight.Black, fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "STEP $index",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFFB22222),
                    letterSpacing = 2.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge.copy(lineHeight = 32.sp),
                color = Color.White
            )

            if (!tip.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    color = Color(0xFFC5A059).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFC5A059).copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.AutoAwesome, null, tint = Color(0xFFC5A059), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CHEF'S TIP: $tip",
                            style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                            color = Color(0xFFC5A059)
                        )
                    }
                }
            }

            if (timeInMinutes != null) {
                Spacer(modifier = Modifier.height(24.dp))
                ProCookingTimer(durationMinutes = timeInMinutes)
            }
        }
    }
}

@Composable
fun ProCookingTimer(durationMinutes: Int) {
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
    
    val infiniteTransition = rememberInfiniteTransition(label = "Pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isRunning) 1.05f else 1f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "PulseScale"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFB22222).copy(alpha = 0.1f))
            .border(1.dp, Color(0xFFB22222).copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.LocalFireDepartment, 
            contentDescription = null, 
            tint = Color(0xFFB22222),
            modifier = Modifier.graphicsLayer { scaleX = pulseScale; scaleY = pulseScale }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = timeString,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Light),
            color = Color(0xFFB22222),
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = { isRunning = !isRunning },
            modifier = Modifier
                .clip(CircleShape)
                .background(if (isRunning) Color.White.copy(alpha = 0.1f) else Color(0xFFB22222))
        ) {
            Icon(
                if (isRunning) Icons.Default.Timer else Icons.Default.Timer,
                contentDescription = null,
                tint = if (isRunning) Color(0xFFB22222) else Color.White
            )
        }
    }
}
