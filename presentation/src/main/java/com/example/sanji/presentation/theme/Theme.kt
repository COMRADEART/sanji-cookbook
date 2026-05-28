package com.example.sanji.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Baratie Heritage Colors
val BaratieBlue = Color(0xFF0A2463)
val BaratieGold = Color(0xFFFFD700)
val BaratieRed = Color(0xFFFB3640)
val KitchenWhite = Color(0xFFF8F9FA)
val ProfessionalInk = Color(0xFF1D1E1F)
val SteelGray = Color(0xFF6C757D)

private val LightColorScheme = lightColorScheme(
    primary = BaratieBlue,
    onPrimary = Color.White,
    secondary = BaratieGold,
    onSecondary = ProfessionalInk,
    tertiary = BaratieRed,
    surface = KitchenWhite,
    onSurface = ProfessionalInk,
    background = KitchenWhite,
    onBackground = ProfessionalInk,
    secondaryContainer = Color(0xFFE5E7EB), // Light gray for previews
    tertiaryContainer = Color(0xFFFFEBEE)   // Soft red for tips
)

private val DarkColorScheme = darkColorScheme(
    primary = BaratieGold, // Invert for dark mode
    onPrimary = ProfessionalInk,
    secondary = BaratieBlue,
    onSecondary = Color.White,
    tertiary = BaratieRed,
    surface = Color(0xFF121212),
    onSurface = KitchenWhite
)

val BaratieTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Serif, // Placeholder for Playfair Display
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif, // Placeholder for Montserrat
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default, // Placeholder for Inter
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
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
