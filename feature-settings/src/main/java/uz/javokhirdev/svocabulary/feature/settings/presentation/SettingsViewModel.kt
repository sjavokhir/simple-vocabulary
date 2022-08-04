package uz.javokhirdev.svocabulary.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uz.javokhirdev.svocabulary.core.data.DispatcherProvider
import uz.javokhirdev.svocabulary.feature.sets.domain.usecase.SetsUseCases
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val setsUseCases: SetsUseCases,
    private val provider: DispatcherProvider
) : ViewModel() {

    val uiState = MutableStateFlow(SettingsState())

    fun handleEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.OnImportClick -> importData()
            SettingsEvent.OnExportClick -> exportData()
            SettingsEvent.OnContactClick -> {}
            SettingsEvent.OnResetClick -> resetProgress()
            SettingsEvent.OnShareClick -> {}
        }
    }

    private fun resetProgress() {
        viewModelScope.launch(provider.io()) {
            setsUseCases.deleteAllData().collect()
        }
    }

    private fun importData() {

    }

    private fun exportData() {

    }

//    fun exportDatabaseToCSVFile(context: Context) {
//        viewModelScope.launch {
//            exportData.value = UIState.loading(true)
//
//            setsRepository.getSetsWithCards().collectLatest { list ->
//                if (list.isNotEmpty()) {
//                    list.forEach { generateFile(context, it) }
//                }
//
//                exportData.value = UIState.loading(false)
//                exportData.value = UIState.success(list.isNotEmpty())
//                exportData.value = UIState.Idle
//            }
//        }
//    }

//    private fun generateFile(context: Context, model: SetWithCardsModel) {
//        try {
//            context.generateCsvFile(model.set.title.orEmpty()) {
//                csvWriter().open(this, append = false) {
//                    writeRow(listOf("id", "setId", "term", "definition"))
//
//                    model.cards.forEach { card ->
//                        writeRow(listOf(card.id, card.setId, card.term, card.definition))
//                    }
//                }
//            }
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }
//    }
}