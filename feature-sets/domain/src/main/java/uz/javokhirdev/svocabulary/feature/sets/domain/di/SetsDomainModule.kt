package uz.javokhirdev.svocabulary.feature.sets.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import uz.javokhirdev.svocabulary.feature.sets.domain.repository.SetsRepository
import uz.javokhirdev.svocabulary.feature.sets.domain.usecase.*

@Module
@InstallIn(ViewModelComponent::class)
object SetsDomainModule {

    @[Provides ViewModelScoped]
    fun provideSetsUseCases(repository: SetsRepository): SetsUseCases {
        return SetsUseCases(
            getSetsWithCount = GetSetsWithCount(repository),
            getSetById = GetSetById(repository),
            upsertSet = UpsertSet(repository),
            deleteSet = DeleteSet(repository),
            deleteAllData = DeleteAllData(repository)
        )
    }
}