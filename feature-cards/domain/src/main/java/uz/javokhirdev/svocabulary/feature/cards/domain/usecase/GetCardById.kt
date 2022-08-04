package uz.javokhirdev.svocabulary.feature.cards.domain.usecase

import uz.javokhirdev.svocabulary.feature.cards.domain.repository.CardsRepository

class GetCardById(
    private val repository: CardsRepository
) {

    operator fun invoke(cardId: Long) = repository.getCardById(cardId)
}