package uz.javokhirdev.svocabulary.core.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @[Provides Singleton]
    fun providesVocabDatabase(
        @ApplicationContext context: Context,
    ): VocabDatabase = Room.databaseBuilder(
        context,
        VocabDatabase::class.java,
        "svocabulary-db"
    ).build()
}
