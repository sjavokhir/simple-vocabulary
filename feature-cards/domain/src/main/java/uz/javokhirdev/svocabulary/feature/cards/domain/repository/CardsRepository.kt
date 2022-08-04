package uz.javokhirdev.svocabulary.feature.cards.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.javokhirdev.svocabulary.core.model.CardModel

interface CardsRepository {

    fun getCards(setId: Long): Flow<List<CardModel>>

    fun getCardById(cardId: Long): Flow<CardModel>

    suspend fun upsertCard(model: CardModel): Flow<Boolean>

    suspend fun delete(cardId: Long): Flow<Boolean>

    suspend fun deleteCardsBySetId(setId: Long): Flow<Boolean>
}