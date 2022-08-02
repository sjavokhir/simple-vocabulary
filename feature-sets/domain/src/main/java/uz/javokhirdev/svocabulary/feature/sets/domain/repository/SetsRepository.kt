package uz.javokhirdev.svocabulary.feature.sets.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.javokhirdev.svocabulary.core.model.SetModel
import uz.javokhirdev.svocabulary.core.model.SetWithCardsModel

interface SetsRepository {

    fun getSets(): Flow<List<SetModel>>

    fun getSetsWithCount(): Flow<List<SetWithCardsModel>>

    fun getSetById(id: Long): Flow<SetModel>

    suspend fun upsertSet(obj: SetModel): Flow<Boolean>

    suspend fun delete(setId: Long): Flow<Boolean>

    suspend fun deleteAll(): Flow<Boolean>
}