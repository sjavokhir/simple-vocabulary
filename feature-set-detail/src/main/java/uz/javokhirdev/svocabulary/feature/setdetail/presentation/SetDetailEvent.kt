package uz.javokhirdev.svocabulary.feature.setdetail.presentation

sealed class SetDetailEvent {
    data class TitleChanged(val title: String) : SetDetailEvent()
    data class DescriptionChanged(val description: String) : SetDetailEvent()

    object SaveClick : SetDetailEvent()
}