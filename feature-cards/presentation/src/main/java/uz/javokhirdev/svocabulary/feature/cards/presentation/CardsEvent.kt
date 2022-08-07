package uz.javokhirdev.svocabulary.feature.cards.presentation

import uz.javokhirdev.svocabulary.core.model.CardModel

sealed class CardsEvent {
    data class CardLongClick(val cardModel: CardModel? = null) : CardsEvent()
    data class CardDeleteClick(val cardId: Long?) : CardsEvent()
    object ClearAllClick : CardsEvent()

    data class OnClearAllDialog(val isOpen: Boolean) : CardsEvent()
}