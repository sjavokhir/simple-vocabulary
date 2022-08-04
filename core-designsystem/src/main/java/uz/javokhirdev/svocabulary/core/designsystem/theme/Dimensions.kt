package uz.javokhirdev.svocabulary.core.designsystem.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
    val default: Dp = 0.dp,
    val stroke: Dp = 1.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val extraNormal: Dp = 12.dp,
    val normal: Dp = 16.dp,
    val medium: Dp = 24.dp,
    val large: Dp = 32.dp
)

val LocalSpacing = compositionLocalOf { Dimensions() }