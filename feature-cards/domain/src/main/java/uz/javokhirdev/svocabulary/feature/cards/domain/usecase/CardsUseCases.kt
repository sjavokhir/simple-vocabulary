package uz.javokhirdev.svocabulary.feature.cards.domain.usecase

data class CardsUseCases(
    val getCards: GetCards,
    val getCardById: GetCardById,
    val upsertCard: UpsertCard,
    val deleteCard: DeleteCard,
    val deleteCardsBySetId: DeleteCardsBySetId,
)
