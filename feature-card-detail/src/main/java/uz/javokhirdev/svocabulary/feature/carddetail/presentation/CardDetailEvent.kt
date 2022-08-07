package uz.javokhirdev.svocabulary.feature.carddetail.presentation

sealed class CardDetailEvent {
    data class TermChanged(val term: String) : CardDetailEvent()
    data class DefinitionChanged(val definition: String) : CardDetailEvent()

    object SaveClick : CardDetailEvent()
}