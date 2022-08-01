package uz.javokhirdev.svocabulary.core.database.dao

import androidx.room.*
import uz.javokhirdev.svocabulary.core.database.model.SetEntity
import uz.javokhirdev.svocabulary.core.database.model.SetWithCards
import uz.javokhirdev.svocabulary.core.database.util.upsert

@Dao
interface SetsDao {

    @Query(value = "SELECT * FROM sets ORDER BY created_at DESC")
    suspend fun getSets(): List<SetEntity>

    @Transaction
    @Query(value = "SELECT * FROM sets")
    suspend fun getSetsWithCards(): List<SetWithCards>

    @Query(value = "SELECT * FROM sets WHERE set_id = :id")
    suspend fun getSetById(id: Long): SetEntity?

    /**
     * Inserts [obj] into the db if they don't exist, and ignores those that do
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(obj: SetEntity): Long

    /**
     * Updates [obj] in the db that match the primary key, and no-ops if they don't
     */
    @Update
    suspend fun update(obj: SetEntity)

    @Transaction
    suspend fun upsertSet(obj: SetEntity) = upsert(
        item = obj,
        insertMany = ::insert,
        updateMany = ::update
    )

    /**
     * Deletes sets in the db matching the specified [setId]
     */
    @Query(value = "DELETE FROM sets WHERE set_id = :setId")
    suspend fun delete(setId: Long)

    /**
     * Deletes all sets
     */
    @Query(value = "DELETE FROM sets")
    suspend fun deleteAll()
}