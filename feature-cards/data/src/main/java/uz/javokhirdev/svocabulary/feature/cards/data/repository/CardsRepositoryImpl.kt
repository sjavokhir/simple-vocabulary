package uz.javokhirdev.svocabulary.feature.cards.data.repository

import kotlinx.coroutines.flow.*
import uz.javokhirdev.svocabulary.core.data.DispatcherProvider
import uz.javokhirdev.svocabulary.core.database.dao.CardsDao
import uz.javokhirdev.svocabulary.core.database.model.asCardModel
import uz.javokhirdev.svocabulary.core.database.model.asEntity
import uz.javokhirdev.svocabulary.core.model.CardModel
import uz.javokhirdev.svocabulary.feature.cards.domain.repository.CardsRepository

class CardsRepositoryImpl(
    private val cardsDao: CardsDao,
    private val provider: DispatcherProvider
) : CardsRepository {

    override fun getCards(setId: Long): Flow<List<CardModel>> {
        return cardsDao.getCards(setId).map { list ->
            list.map { it.asCardModel() }
        }
    }

    override fun getCardById(cardId: Long): Flow<CardModel> {
        return cardsDao.getCardById(cardId).map { it.asCardModel() }
    }

    override suspend fun upsertCard(model: CardModel): Flow<Boolean> = flow {
        cardsDao.upsertCard(model.asEntity())

        emit(true)
    }.catch {
        emit(false)
    }.flowOn(provider.io())

    override suspend fun delete(cardId: Long): Flow<Boolean> = flow {
        cardsDao.delete(cardId)

        emit(true)
    }.catch {
        emit(false)
    }.flowOn(provider.io())

    override suspend fun deleteCardsBySetId(setId: Long): Flow<Boolean> = flow {
        cardsDao.deleteCardsBySetId(setId)

        emit(true)
    }.catch {
        emit(false)
    }.flowOn(provider.io())
}