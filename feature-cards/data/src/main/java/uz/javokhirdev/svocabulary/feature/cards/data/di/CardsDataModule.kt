package uz.javokhirdev.svocabulary.feature.cards.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.javokhirdev.svocabulary.core.data.DispatcherProvider
import uz.javokhirdev.svocabulary.core.database.VocabDatabase
import uz.javokhirdev.svocabulary.feature.cards.domain.repository.CardsRepository
import uz.javokhirdev.svocabulary.feature.cards.data.repository.CardsRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CardsDataModule {

    @[Provides Singleton]
    fun provideCardsRepository(
        database: VocabDatabase,
        provider: DispatcherProvider
    ): CardsRepository {
        return CardsRepositoryImpl(
            cardsDao = database.cardsDao(),
            provider = provider
        )
    }
}