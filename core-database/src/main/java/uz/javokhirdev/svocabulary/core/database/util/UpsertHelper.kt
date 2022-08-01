package uz.javokhirdev.svocabulary.core.database.util

/**
 * Performs an upsert by first attempting to insert [item] using [insertMany] with the the result
 * of the inserts returned.
 *
 * Items that were not inserted due to conflicts are then updated using [updateMany]
 */
suspend fun <T> upsert(
    item: T,
    insertMany: suspend (T) -> Long,
    updateMany: suspend (T) -> Unit,
) {
    val insertResult = insertMany(item)

    val updateList = if (insertResult == -1L) item else null
    if (updateList != null) updateMany(updateList)
}
