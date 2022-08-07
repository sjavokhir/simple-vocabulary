package uz.javokhirdev.svocabulary.feature.cards.presentation

import uz.javokhirdev.svocabulary.core.model.CardModel
import uz.javokhirdev.svocabulary.core.model.SetModel

data class CardsState(
    val isLoading: Boolean = false,
    val set: SetModel = SetModel(),
    val cards: List<CardModel> = emptyList(),
    val lastLongClickedCardModel: CardModel? = null,
    val isOpenClearAllDialog: Boolean = false
)