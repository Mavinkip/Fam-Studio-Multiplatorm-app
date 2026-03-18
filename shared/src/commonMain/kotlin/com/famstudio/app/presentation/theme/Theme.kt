package com.famstudio.app.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object FamColors {
    val PinterestRed   = Color(0xFFE60023)
    val Background     = Color(0xFFFFFFFF)
    val Surface        = Color(0xFFFFFFFF)
    val SurfaceVariant = Color(0xFFF0F0F0)
    val Border         = Color(0xFFDDDDDD)
    val TextPrimary    = Color(0xFF111111)
    val TextMuted      = Color(0xFF767676)
    val Error          = Color(0xFFE60023)
}

private val LightColorScheme = lightColorScheme(
    primary        = FamColors.PinterestRed,
    secondary      = FamColors.PinterestRed,
    background     = Color(0xFFFFFFFF),
    surface        = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFF0F0F0),
    onPrimary      = Color.White,
    onBackground   = Color(0xFF111111),
    onSurface      = Color(0xFF111111),
    error          = FamColors.PinterestRed,
    outline        = Color(0xFFDDDDDD),
    outlineVariant = Color(0xFFEEEEEE)
)

private val DarkColorScheme = darkColorScheme(
    primary        = FamColors.PinterestRed,
    secondary      = FamColors.PinterestRed,
    background     = Color(0xFF121212),
    surface        = Color(0xFF1E1E1E),
    surfaceVariant = Color(0xFF2A2A2A),
    onPrimary      = Color.White,
    onBackground   = Color(0xFFF5F5F5),
    onSurface      = Color(0xFFF5F5F5),
    error          = FamColors.PinterestRed,
    outline        = Color(0xFF3A3A3A),
    outlineVariant = Color(0xFF2A2A2A)
)

@Composable
fun FamTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content:   @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        content     = content
    )
}