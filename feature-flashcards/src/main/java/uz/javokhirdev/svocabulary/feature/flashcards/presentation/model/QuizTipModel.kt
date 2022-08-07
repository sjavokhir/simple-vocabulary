package uz.javokhirdev.svocabulary.feature.flashcards.presentation.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class QuizTipModel(
    val title: Int,
    val description: Int,
    val icon: ImageVector,
    val tint: Color
)