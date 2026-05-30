package com.example.sanji.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Stitch-Synchronized "Black-Leg" Palette
val SanjiBlack = Color(0xFF1A1A1B)
val BaratieGold = Color(0xFFD4AF37)
val AllBlueTeal = Color(0xFF2C5F78)
val ChefCream = Color(0xFFF5F5F0)
val DiableJambeRed = Color(0xFFB22222)
val HeatOrange = Color(0xFFFF4500)

private val LightColorScheme = lightColorScheme(
    primary = SanjiBlack,
    onPrimary = BaratieGold,
    secondary = BaratieGold,
    onSecondary = SanjiBlack,
    tertiary = AllBlueTeal,
    surface = ChefCream,
    onSurface = SanjiBlack,
    background = ChefCream,
    onBackground = SanjiBlack,
    error = DiableJambeRed,
    secondaryContainer = Color(0xFFE5E5E0),
    tertiaryContainer = Color.White.copy(alpha = 0.8f) // Refined glass for chat
)

private val DarkColorScheme = darkColorScheme(
    primary = BaratieGold,
    onPrimary = SanjiBlack,
    secondary = SanjiBlack,
    onSecondary = BaratieGold,
    tertiary = AllBlueTeal,
    surface = Color(0xFF121212),
    onSurface = ChefCream
)

val BaratieTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Serif, // Playfair Display / Cinzel
        fontWeight = FontWeight.Black,
        fontSize = 44.sp,
        lineHeight = 52.sp,
        letterSpacing = (-1.5).sp // Tighter for more impact
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.5).sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif, // Montserrat
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif, // Montserrat
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 1.5.sp // Elegant spacing for menus
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default, // Lato
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.2.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default, // Lato
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.2.sp
    )
)

@Composable
fun BaratieTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = BaratieTypography,
        content = content
    )
}
