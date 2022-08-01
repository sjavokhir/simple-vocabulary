package uz.javokhirdev.svocabulary.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.javokhirdev.svocabulary.core.database.dao.CardsDao
import uz.javokhirdev.svocabulary.core.database.dao.SetsDao
import uz.javokhirdev.svocabulary.core.database.model.CardEntity
import uz.javokhirdev.svocabulary.core.database.model.SetEntity

@Database(
    entities = [
        SetEntity::class,
        CardEntity::class,
    ],
    version = 1
)
abstract class VocabDatabase : RoomDatabase() {

    abstract fun setsDao(): SetsDao

    abstract fun cardsDao(): CardsDao
}