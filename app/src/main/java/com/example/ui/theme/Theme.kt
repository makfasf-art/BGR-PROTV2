package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonViolet,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF2E1065),
    onPrimaryContainer = NeonVioletLight,
    secondary = NeonCyan,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF083344),
    onSecondaryContainer = NeonCyanLight,
    tertiary = NeonPink,
    onTertiary = Color.White,
    background = SpaceBlack,
    onBackground = TextWhite,
    surface = SpaceDark,
    onSurface = TextWhite,
    surfaceVariant = SpaceGlass,
    onSurfaceVariant = TextGrayLight,
    outline = SpaceCardBorder,
    outlineVariant = Color(0x3367E8F9)
)

@Composable
fun BotGeneratorTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
