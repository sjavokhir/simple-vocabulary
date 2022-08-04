package uz.javokhirdev.svocabulary.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.javokhirdev.svocabulary.core.model.CardModel

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "card_id")
    val id: Long = 0L,

    @ColumnInfo(name = "card_set_id")
    val setId: Long? = null,

    @ColumnInfo(name = "card_term")
    val term: String? = null,

    @ColumnInfo(name = "card_definition")
    val definition: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

fun CardEntity?.asCardModel() = CardModel(
    id = this?.id,
    setId = this?.setId,
    term = this?.term,
    definition = this?.definition
)

fun CardModel.asEntity() = CardEntity(
    id = this.id ?: 0L,
    setId = this.setId,
    term = this.term,
    definition = this.definition
)