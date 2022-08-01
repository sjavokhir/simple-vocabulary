package uz.javokhirdev.svocabulary.feature.sets.presentation

import uz.javokhirdev.svocabulary.core.model.SetModel

data class SetsState(
    val isLoading: Boolean = false,
    val sets: List<SetModel> = emptyList()
)