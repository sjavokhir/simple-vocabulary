package uz.javokhirdev.svocabulary.core.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uz.javokhirdev.svocabulary.core.database.model.CardEntity
import uz.javokhirdev.svocabulary.core.database.util.upsert

@Dao
interface CardsDao {

    @Query(
        value = """
            SELECT * FROM cards WHERE card_set_id = :setId 
            ORDER BY created_at DESC
        """
    )
    fun getCards(setId: Long): Flow<List<CardEntity>>

    @Query(
        value = """
            SELECT * FROM cards WHERE card_set_id = :setId 
            AND (card_term LIKE :keywords OR card_definition LIKE :keywords)
            ORDER BY created_at DESC
        """
    )
    fun getCards(setId: Long, keywords: String = ""): Flow<List<CardEntity>>

    @Query(value = "SELECT * FROM cards WHERE card_id = :id")
    fun getCardById(id: Long): Flow<CardEntity?>

    /**
     * Inserts [entity] into the db if they don't exist, and ignores those that do
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: CardEntity): Long

    /**
     * Updates [entity] in the db that match the primary key, and no-ops if they don't
     */
    @Update
    suspend fun update(entity: CardEntity)

    @Transaction
    suspend fun upsertCard(entity: CardEntity) = upsert(
        item = entity,
        insertMany = ::insert,
        updateMany = ::update
    )

    /**
     * Deletes cards in the db matching the specified [id]
     */
    @Query(value = "DELETE FROM cards WHERE card_id = :id")
    suspend fun delete(id: Long)

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