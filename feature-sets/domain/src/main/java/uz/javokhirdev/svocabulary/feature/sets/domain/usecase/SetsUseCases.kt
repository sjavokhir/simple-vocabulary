package uz.javokhirdev.svocabulary.feature.sets.domain.usecase

data class SetsUseCases(
    val getSets: GetSets,
    val getSetById: GetSetById,
    val upsertSet: UpsertSet,
    val deleteSet: DeleteSet,
    val deleteAllData: DeleteAllData,
)
