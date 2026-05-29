package com.example.sanji.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Baratie Heritage V2 Luxurious Colors
val MidnightAtlantic = Color(0xFF05163D)
val MetallicGold = Color(0xFFD4AF37)
val CrimsonPassion = Color(0xFFD72638)
val PristineWhite = Color(0xFFFFFFFF)
val StainlessGray = Color(0xFFF4F7F9)
val ProfessionalInk = Color(0xFF0D0E10)
val GlassWhite = Color(0xB3FFFFFF) // 70% opacity for glassmorphism

private val LightColorScheme = lightColorScheme(
    primary = MidnightAtlantic,
    onPrimary = Color.White,
    secondary = MetallicGold,
    onSecondary = ProfessionalInk,
    tertiary = CrimsonPassion,
    surface = PristineWhite,
    onSurface = ProfessionalInk,
    background = StainlessGray,
    onBackground = ProfessionalInk,
    secondaryContainer = Color(0xFFE5E7EB),
    tertiaryContainer = GlassWhite
)

private val DarkColorScheme = darkColorScheme(
    primary = MetallicGold,
    onPrimary = ProfessionalInk,
    secondary = MidnightAtlantic,
    onSecondary = Color.White,
    tertiary = CrimsonPassion,
    surface = Color(0xFF0D0E10),
    onSurface = PristineWhite
)

val BaratieTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Serif, // Playfair Display equivalent
        fontWeight = FontWeight.Black,
        fontSize = 42.sp,
        lineHeight = 48.sp,
        letterSpacing = (-0.5).sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif, // Montserrat equivalent
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 1.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default, // Inter equivalent
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 30.sp // Luxurious 1.6x line height
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.sp
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
