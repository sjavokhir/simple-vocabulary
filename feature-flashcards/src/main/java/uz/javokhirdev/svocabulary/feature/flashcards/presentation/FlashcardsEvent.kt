package uz.javokhirdev.svocabulary.feature.flashcards.presentation

import uz.javokhirdev.svocabulary.core.model.CardModel
import uz.javokhirdev.svocabulary.feature.flashcards.presentation.components.SwipedOutDirection

sealed class FlashcardsEvent {
    object SideClick : FlashcardsEvent()

    data class RemoveCard(
        val item: CardModel,
        val direction: SwipedOutDirection
    ) : FlashcardsEvent()

    object Empty : FlashcardsEvent()

    data class OnTipsDialog(val isOpen: Boolean) : FlashcardsEvent()
}