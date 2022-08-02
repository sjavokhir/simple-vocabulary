package uz.javokhirdev.svocabulary.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import uz.javokhirdev.svocabulary.core.model.SetWithCardsModel

data class SetWithCardsEntity(
    @Embedded
    val set: SetEntity? = null,
    @ColumnInfo(name = "cards_count")
    val cardsCount: Int? = null
)

fun SetWithCardsEntity.asSetWithCardsModel() = SetWithCardsModel(
    set = this.set.asSetModel(),
    cardsCount = this.cardsCount
)