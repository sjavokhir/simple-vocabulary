package uz.javokhirdev.svocabulary.feature.sets.domain.usecase

import uz.javokhirdev.svocabulary.feature.sets.domain.repository.SetsRepository

class DeleteSet(
    private val repository: SetsRepository
) {

    suspend operator fun invoke(setId: Long) = repository.delete(setId)
}