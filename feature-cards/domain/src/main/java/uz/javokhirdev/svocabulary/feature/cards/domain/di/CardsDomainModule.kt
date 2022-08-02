package uz.javokhirdev.svocabulary.feature.cards.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import uz.javokhirdev.svocabulary.feature.cards.domain.repository.CardsRepository
import uz.javokhirdev.svocabulary.feature.cards.domain.usecase.*

@Module
@InstallIn(ViewModelComponent::class)
object CardsDomainModule {

    @[Provides ViewModelScoped]
    fun provideCardsUseCases(repository: CardsRepository): CardsUseCases {
        return CardsUseCases(
            getCards = GetCards(repository),
            getCardById = GetCardById(repository),
            upsertCard = UpsertCard(repository),
            deleteCard = DeleteCard(repository),
            deleteCardsBySetId = DeleteCardsBySetId(repository)
        )
    }
}