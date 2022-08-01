package uz.javokhirdev.svocabulary.feature.sets.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.javokhirdev.svocabulary.core.model.SetModel

interface SetsRepository {

    fun getSets(): Flow<List<SetModel>>

    fun getSetById(id: Long): Flow<SetModel>

    suspend fun upsertSet(obj: SetModel): Flow<Boolean>

    suspend fun delete(setId: Long): Flow<Boolean>

    suspend fun deleteAll(): Flow<Boolean>
}