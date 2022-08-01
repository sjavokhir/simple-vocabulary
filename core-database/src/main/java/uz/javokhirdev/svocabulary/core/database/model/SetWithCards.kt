package uz.javokhirdev.svocabulary.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import uz.javokhirdev.svocabulary.core.model.CardModel
import uz.javokhirdev.svocabulary.core.model.SetModel
import uz.javokhirdev.svocabulary.core.model.SetWithCardsModel

data class SetWithCards(
    @Embedded val set: SetEntity,
    @Relation(
        parentColumn = "set_id",
        entityColumn = "card_set_id"
    )
    val cards: List<CardEntity>
)

fun SetWithCards?.asSetWithCardsModel() = SetWithCardsModel(
    set = SetModel(
        id = this?.set?.id,
        title = this?.set?.title,
        description = this?.set?.description
    ),
    cards = this?.cards?.map { card ->
        CardModel(
            id = card.id,
            setId = card.setId,
            term = card.term,
            definition = card.definition
        )
    }
)