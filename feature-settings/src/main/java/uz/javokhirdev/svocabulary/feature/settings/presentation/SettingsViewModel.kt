package uz.javokhirdev.svocabulary.feature.settings.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.javokhirdev.svocabulary.core.data.DispatcherProvider
import uz.javokhirdev.svocabulary.feature.sets.domain.usecase.SetsUseCases
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val setsUseCases: SetsUseCases,
    private val provider: DispatcherProvider
) : ViewModel() {

    var uiState by mutableStateOf(SettingsState())
        private set

    fun handleEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.ImportClick -> importData()
            SettingsEvent.ExportClick -> exportData()
            SettingsEvent.ContactClick -> {}
            SettingsEvent.ResetClick -> resetProgress()
            SettingsEvent.ShareClick -> {}
            is SettingsEvent.OnResetDialog -> {
                uiState = uiState.copy(isOpenResetDialog = event.isOpen)
            }
        }
    }

    private fun resetProgress() {
        uiState = uiState.copy(isOpenResetDialog = false)

        viewModelScope.launch {
            withContext(provider.io()) {
                setsUseCases.deleteAllData().collect()
            }
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