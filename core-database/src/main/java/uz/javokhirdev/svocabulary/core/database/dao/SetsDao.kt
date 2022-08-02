package uz.javokhirdev.svocabulary.core.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uz.javokhirdev.svocabulary.core.database.model.SetEntity
import uz.javokhirdev.svocabulary.core.database.model.SetWithCardsEntity
import uz.javokhirdev.svocabulary.core.database.util.upsert

@Dao
interface SetsDao {

    @Transaction
    @Query(
        value = """
        SELECT *, (SELECT COUNT(*) FROM cards 
        WHERE card_set_id = sets.set_id) AS cards_count FROM sets 
        ORDER BY created_at DESC
    """
    )
    fun getSetsWithCount(): Flow<List<SetWithCardsEntity>>

    @Query(value = "SELECT * FROM sets WHERE set_id = :id")
    fun getSetById(id: Long): Flow<SetEntity?>

    /**
     * Inserts [entity] into the db if they don't exist, and ignores those that do
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: SetEntity): Long

    /**
     * Updates [entity] in the db that match the primary key, and no-ops if they don't
     */
    @Update
    suspend fun update(entity: SetEntity)

    @Transaction
    suspend fun upsertSet(entity: SetEntity) = upsert(
        item = entity,
        insertMany = ::insert,
        updateMany = ::update
    )

    /**
     * Deletes sets in the db matching the specified [id]
     */
    @Query(value = "DELETE FROM sets WHERE set_id = :id")
    suspend fun delete(id: Long)

    /**
     * Deletes all sets
     */
    @Query(value = "DELETE FROM sets")
    suspend fun deleteAll()
}