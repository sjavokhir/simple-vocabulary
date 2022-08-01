package uz.javokhirdev.svocabulary.feature.sets.domain.usecase

import uz.javokhirdev.svocabulary.feature.sets.domain.repository.SetsRepository

class GetSetById(
    private val repository: SetsRepository
) {

    operator fun invoke(setId: Long) = repository.getSetById(setId)
}