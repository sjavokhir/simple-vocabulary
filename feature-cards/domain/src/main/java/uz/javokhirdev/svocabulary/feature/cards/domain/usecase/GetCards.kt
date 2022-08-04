package uz.javokhirdev.svocabulary.feature.cards.domain.usecase

import uz.javokhirdev.svocabulary.feature.cards.domain.repository.CardsRepository

class GetCards(
    private val repository: CardsRepository
) {

    operator fun invoke(setId: Long) = repository.getCards(setId)
}