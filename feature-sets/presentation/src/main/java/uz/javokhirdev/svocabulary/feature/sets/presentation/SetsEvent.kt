package uz.javokhirdev.svocabulary.feature.sets.presentation

import uz.javokhirdev.svocabulary.core.model.SetModel

sealed class SetsEvent {
    data class SetLongClick(val setModel: SetModel? = null) : SetsEvent()
    data class SetDeleteClick(val setId: Long?) : SetsEvent()
}