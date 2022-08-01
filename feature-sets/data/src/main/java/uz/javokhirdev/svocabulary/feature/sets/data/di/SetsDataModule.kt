package uz.javokhirdev.svocabulary.feature.sets.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.javokhirdev.svocabulary.core.data.DispatcherProvider
import uz.javokhirdev.svocabulary.core.database.VocabDatabase
import uz.javokhirdev.svocabulary.feature.sets.data.repository.SetsRepositoryImpl
import uz.javokhirdev.svocabulary.feature.sets.domain.repository.SetsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SetsDataModule {

    @[Provides Singleton]
    fun provideSetsRepository(
        database: VocabDatabase,
        provider: DispatcherProvider
    ): SetsRepository {
        return SetsRepositoryImpl(
            setsDao = database.setsDao(),
            cardsDao = database.cardsDao(),
            provider = provider
        )
    }
}