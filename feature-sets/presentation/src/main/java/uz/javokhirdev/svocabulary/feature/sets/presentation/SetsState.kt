package uz.javokhirdev.svocabulary.feature.sets.presentation

import uz.javokhirdev.svocabulary.core.model.SetModel
import uz.javokhirdev.svocabulary.core.model.SetWithCardsModel

data class SetsState(
    val isLoading: Boolean = false,
    val sets: List<SetWithCardsModel> = emptyList(),
    val lastLongClickedSetModel: SetModel? = null
)