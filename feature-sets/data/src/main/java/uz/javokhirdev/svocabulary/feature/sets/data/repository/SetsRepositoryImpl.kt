package uz.javokhirdev.svocabulary.feature.sets.data.repository

import kotlinx.coroutines.flow.*
import uz.javokhirdev.svocabulary.core.data.DispatcherProvider
import uz.javokhirdev.svocabulary.core.database.dao.CardsDao
import uz.javokhirdev.svocabulary.core.database.dao.SetsDao
import uz.javokhirdev.svocabulary.core.database.model.asEntity
import uz.javokhirdev.svocabulary.core.database.model.asSetModel
import uz.javokhirdev.svocabulary.core.database.model.asSetWithCardsModel
import uz.javokhirdev.svocabulary.core.model.SetModel
import uz.javokhirdev.svocabulary.core.model.SetWithCardsModel
import uz.javokhirdev.svocabulary.feature.sets.domain.repository.SetsRepository

class SetsRepositoryImpl(
    private val setsDao: SetsDao,
    private val cardsDao: CardsDao,
    private val provider: DispatcherProvider
) : SetsRepository {

    override fun getSetsWithCount(): Flow<List<SetWithCardsModel>> {
        return setsDao.getSetsWithCount().map { list ->
            list.map { it.asSetWithCardsModel() }
        }
    }

    override fun getSetById(setId: Long): Flow<SetModel> {
        return setsDao.getSetById(setId).map { it.asSetModel() }
    }

    override suspend fun upsertSet(model: SetModel): Flow<Boolean> = flow {
        setsDao.upsertSet(model.asEntity())

        emit(true)
    }.catch {
        emit(false)
    }.flowOn(provider.io())

    override suspend fun delete(setId: Long): Flow<Boolean> = flow {
        setsDao.delete(setId)
        cardsDao.deleteCardsBySetId(setId)

        emit(true)
    }.catch {
        emit(false)
    }.flowOn(provider.io())

    override suspend fun deleteAll(): Flow<Boolean> = flow {
        setsDao.deleteAll()
        cardsDao.deleteAll()

        emit(true)
    }.catch {
        emit(false)
    }.flowOn(provider.io())
}