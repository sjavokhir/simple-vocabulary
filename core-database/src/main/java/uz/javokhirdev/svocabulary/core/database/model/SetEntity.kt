package uz.javokhirdev.svocabulary.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.javokhirdev.svocabulary.core.model.SetModel

@Entity(tableName = "sets")
data class SetEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "set_id")
    val id: Long = 0L,

    @ColumnInfo(name = "set_title")
    val title: String? = null,

    @ColumnInfo(name = "set_description")
    val description: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

fun SetEntity?.asSetModel() = SetModel(
    id = this?.id,
    title = this?.title,
    description = this?.description
)

fun SetModel.asEntity() = SetEntity(
    id = this.id ?: 0L,
    title = this.title.orEmpty().trim(),
    description = this.description.orEmpty().trim()
)