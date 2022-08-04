package uz.javokhirdev.svocabulary.feature.sets.presentation

sealed class SetsEvent {
    data class OnDeleteClick(val setId: Long?) : SetsEvent()
}