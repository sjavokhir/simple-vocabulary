package uz.javokhirdev.svocabulary.feature.flashcards.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import uz.javokhirdev.svocabulary.core.model.CardModel

data class FlashcardsState(
    val isLoading: Boolean = false,
    val isFinished: Boolean = false,
    val cards: SnapshotStateList<CardModel> = mutableStateListOf(),
    val forgots: SnapshotStateList<CardModel> = mutableStateListOf(),
    val knows: SnapshotStateList<CardModel> = mutableStateListOf(),
    val isFrontSide: Boolean = true,
    val isOpenTipsDialog: Boolean = false
)