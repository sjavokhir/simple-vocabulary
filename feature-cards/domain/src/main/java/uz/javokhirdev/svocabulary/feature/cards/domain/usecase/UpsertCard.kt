package uz.javokhirdev.svocabulary.feature.cards.domain.usecase

import uz.javokhirdev.svocabulary.core.model.CardModel
import uz.javokhirdev.svocabulary.feature.cards.domain.repository.CardsRepository

class UpsertCard(
    private val repository: CardsRepository
) {

    suspend operator fun invoke(model: CardModel) = repository.upsertCard(model)
}