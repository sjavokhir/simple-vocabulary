package uz.javokhirdev.svocabulary.feature.sets.domain.usecase

import uz.javokhirdev.svocabulary.feature.sets.domain.repository.SetsRepository

class GetSets(
    private val repository: SetsRepository
) {

    operator fun invoke() = repository.getSetsWithCount()
}