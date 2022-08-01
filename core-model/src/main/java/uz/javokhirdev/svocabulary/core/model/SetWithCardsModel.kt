package uz.javokhirdev.svocabulary.core.model

data class SetWithCardsModel(
    val set: SetModel,
    val cards: List<CardModel>? = emptyList()
)