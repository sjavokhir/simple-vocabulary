package uz.javokhirdev.svocabulary.feature.sets.domain.usecase

import uz.javokhirdev.svocabulary.feature.sets.domain.repository.SetsRepository

class DeleteAllData(
    private val repository: SetsRepository
) {

    suspend operator fun invoke() = repository.deleteAll()
}