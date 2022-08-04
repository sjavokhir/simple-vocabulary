package uz.javokhirdev.svocabulary.feature.sets.domain.usecase

import uz.javokhirdev.svocabulary.core.model.SetModel
import uz.javokhirdev.svocabulary.feature.sets.domain.repository.SetsRepository

class UpsertSet(
    private val repository: SetsRepository
) {

    suspend operator fun invoke(model: SetModel) = repository.upsertSet(model)
}