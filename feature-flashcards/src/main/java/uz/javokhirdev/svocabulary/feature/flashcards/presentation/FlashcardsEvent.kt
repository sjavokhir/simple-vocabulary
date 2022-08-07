package uz.javokhirdev.svocabulary.feature.flashcards.presentation

import uz.javokhirdev.svocabulary.core.model.CardModel
import uz.javokhirdev.svocabulary.feature.flashcards.presentation.components.SwipedOutDirection

sealed class FlashcardsEvent {
    object InfoClick : FlashcardsEvent()

    data class RemoveCard(
        val item: CardModel,
        val direction: SwipedOutDirection
    ) : FlashcardsEvent()

    object OnEmpty : FlashcardsEvent()
}