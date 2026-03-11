package com.famstudio.app.presentation.theme


import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object FamColors {
    val PinterestRed    = Color(0xFFE60023)
    val Background      = Color(0xFFFFFFFF)
    val Surface         = Color(0xFFFFFFFF)
    val SurfaceVariant  = Color(0xFFF0F0F0)
    val Border          = Color(0xFFDDDDDD)
    val TextPrimary     = Color(0xFF111111)
    val TextMuted       = Color(0xFF767676)
    val Error           = Color(0xFFE60023)
}

private val LightColorScheme = lightColorScheme(
    primary         = FamColors.PinterestRed,
    secondary       = FamColors.PinterestRed,
    background      = FamColors.Background,
    surface         = FamColors.Surface,
    surfaceVariant  = FamColors.SurfaceVariant,
    onPrimary       = Color.White,
    onBackground    = FamColors.TextPrimary,
    onSurface       = FamColors.TextPrimary,
    error           = FamColors.Error,
    outline         = FamColors.Border
)

@Composable
fun FamTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}