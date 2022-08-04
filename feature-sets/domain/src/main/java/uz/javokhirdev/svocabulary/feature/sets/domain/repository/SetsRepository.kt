package uz.javokhirdev.svocabulary.feature.sets.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.javokhirdev.svocabulary.core.model.SetModel
import uz.javokhirdev.svocabulary.core.model.SetWithCardsModel

interface SetsRepository {

    fun getSetsWithCount(): Flow<List<SetWithCardsModel>>

    fun getSetById(setId: Long): Flow<SetModel>

    suspend fun upsertSet(model: SetModel): Flow<Boolean>

    suspend fun delete(setId: Long): Flow<Boolean>

    suspend fun deleteAll(): Flow<Boolean>
}