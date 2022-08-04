package uz.javokhirdev.svocabulary.feature.cards.presentation

sealed class CardsEvent {
    object OnClearAllClick : CardsEvent()
}