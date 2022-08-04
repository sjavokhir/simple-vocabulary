package uz.javokhirdev.svocabulary.feature.cards.domain.usecase

import uz.javokhirdev.svocabulary.feature.cards.domain.repository.CardsRepository

class DeleteCardsBySetId(
    private val repository: CardsRepository
) {

    suspend operator fun invoke(setId: Long) = repository.deleteCardsBySetId(setId)
}