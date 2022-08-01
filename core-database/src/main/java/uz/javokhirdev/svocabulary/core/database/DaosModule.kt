package uz.javokhirdev.svocabulary.core.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.javokhirdev.svocabulary.core.database.dao.CardsDao
import uz.javokhirdev.svocabulary.core.database.dao.SetsDao

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun providesSetsDao(database: VocabDatabase): SetsDao = database.setsDao()

    @Provides
    fun providesCardsDao(database: VocabDatabase): CardsDao = database.cardsDao()
}
