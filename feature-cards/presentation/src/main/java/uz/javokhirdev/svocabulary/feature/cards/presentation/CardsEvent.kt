package uz.javokhirdev.svocabulary.feature.cards.presentation

sealed class CardsEvent {
    data class OnDeleteClick(val cardId: Long?) : CardsEvent()
    object OnClearAllClick : CardsEvent()
}