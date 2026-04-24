package com.norton.scamdetector.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val NortonColorScheme = lightColorScheme(
    primary = NortonColors.PrimaryYellow,
    onPrimary = NortonColors.TextPrimary,
    secondary = NortonColors.SafeGreen,
    onSecondary = NortonColors.CardBackground,
    background = NortonColors.Background,
    onBackground = NortonColors.TextPrimary,
    surface = NortonColors.CardBackground,
    onSurface = NortonColors.TextPrimary,
    error = NortonColors.DangerousRed,
    onError = NortonColors.CardBackground,
    outline = NortonColors.DividerColor,
)

@Composable
fun NortonTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NortonColorScheme,
        typography = NortonTypography,
        shapes = NortonShapes,
        content = content
    )
}