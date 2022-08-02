package uz.javokhirdev.svocabulary.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import uz.javokhirdev.svocabulary.core.ui.R

/**
 * Vocab in Android typography.
 */
internal val NiaTypography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.W400,
        fontFamily = fontFamily,
        fontSize = 57.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.W400,
        fontFamily = fontFamily,
        fontSize = 45.sp
    ),
    displaySmall = TextStyle(
        fontWeight = FontWeight.W400,
        fontFamily = fontFamily,
        fontSize = 36.sp
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.W400,
        fontFamily = fontFamily,
        fontSize = 32.sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.W400,
        fontFamily = fontFamily,
        fontSize = 28.sp
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.W400,
        fontFamily = fontFamily,
        fontSize = 24.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.W700,
        fontFamily = fontFamily,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.W700,
        fontFamily = fontFamily,
        fontSize = 16.sp,
        letterSpacing = 0.1.sp
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.W500,
        fontFamily = fontFamily,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.W400,
        fontFamily = fontFamily,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.W400,
        fontFamily = fontFamily,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.W400,
        fontFamily = fontFamily,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.W400,
        fontFamily = fontFamily,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.W400,
        fontFamily = fontFamily,
        fontSize = 12.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 10.sp
    )
)

private val fontFamily
    get() = FontFamily(
        Font(R.font.medium, FontWeight.Light),
        Font(R.font.medium, FontWeight.Normal),
        Font(R.font.medium, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.medium, FontWeight.Medium),
        Font(R.font.bold, FontWeight.Bold)
    )
