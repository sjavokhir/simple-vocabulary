package uz.javokhirdev.svocabulary.feature.sets.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.javokhirdev.svocabulary.core.data.DispatcherProvider
import uz.javokhirdev.svocabulary.core.database.dao.CardsDao
import uz.javokhirdev.svocabulary.core.database.dao.SetsDao
import uz.javokhirdev.svocabulary.core.database.model.asEntity
import uz.javokhirdev.svocabulary.core.database.model.asSetModel
import uz.javokhirdev.svocabulary.core.model.SetModel
import uz.javokhirdev.svocabulary.feature.sets.domain.repository.SetsRepository

class SetsRepositoryImpl(
    private val setsDao: SetsDao,
    private val cardsDao: CardsDao,
    private val provider: DispatcherProvider
) : SetsRepository {

    override fun getSets(): Flow<List<SetModel>> = flow {
        val sets = setsDao.getSets().map { it.asSetModel() }

        emit(sets)
    }.catch {
        emit(emptyList())
    }.flowOn(provider.io())

    override fun getSetById(id: Long): Flow<SetModel> = flow {
        val model = setsDao.getSetById(id).asSetModel()

        emit(model)
    }.catch {
        emit(SetModel())
    }.flowOn(provider.io())

    override suspend fun upsertSet(obj: SetModel): Flow<Boolean> = flow {
        setsDao.upsertSet(obj.asEntity())

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