package uz.javokhirdev.svocabulary.core.database.dao

import androidx.room.*
import uz.javokhirdev.svocabulary.core.database.model.CardEntity
import uz.javokhirdev.svocabulary.core.database.util.upsert

@Dao
interface CardsDao {

    @Query(
        value = """
            SELECT * FROM cards WHERE card_set_id = :setId 
            AND (card_term LIKE :keywords OR card_definition LIKE :keywords)
            ORDER BY created_at DESC
        """
    )
    fun getCards(setId: Long, keywords: String = ""): List<CardEntity>

    @Query(value = "SELECT * FROM cards WHERE card_id = :id")
    fun getCardById(id: Long): CardEntity

    /**
     * Inserts [obj] into the db if they don't exist, and ignores those that do
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(obj: CardEntity): Long

    /**
     * Updates [obj] in the db that match the primary key, and no-ops if they don't
     */
    @Update
    suspend fun update(obj: CardEntity)

    @Transaction
    suspend fun upsertCard(obj: CardEntity) = upsert(
        item = obj,
        insertMany = ::insert,
        updateMany = ::update
    )

    /**
     * Deletes cards in the db matching the specified [cardId]
     */
    @Query(value = "DELETE FROM cards WHERE card_id = :cardId")
    suspend fun delete(cardId: Long)

    /**
     * Deletes cards in the db matching the specified [setId]
     */
    @Query(value = "DELETE FROM cards WHERE card_set_id = :setId")
    suspend fun deleteCardsBySetId(setId: Long)

    /**
     * Deletes all cards
     */
    @Query(value = "DELETE FROM cards")
    suspend fun deleteAll()
}