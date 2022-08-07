package uz.javokhirdev.svocabulary.feature.sets.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.javokhirdev.svocabulary.core.data.DispatcherProvider
import uz.javokhirdev.svocabulary.core.model.SetWithCardsModel
import uz.javokhirdev.svocabulary.feature.sets.domain.usecase.SetsUseCases
import javax.inject.Inject

@HiltViewModel
class SetsViewModel @Inject constructor(
    private val setsUseCases: SetsUseCases,
    private val provider: DispatcherProvider
) : ViewModel() {

    var uiState by mutableStateOf(SetsState())
        private set

    private var getSetsJob: Job? = null

    init {
        getSets()
    }

    fun handleEvent(event: SetsEvent) {
        when (event) {
            is SetsEvent.SetLongClick -> {
                uiState = uiState.copy(lastLongClickedSetModel = event.setModel)
            }
            is SetsEvent.SetDeleteClick -> deleteSet(event.setId)
        }
    }

    private fun getSets() {
        getSetsJob?.cancel()
        getSetsJob = viewModelScope.launch {
            showLoading()

            withContext(provider.io()) {
                setsUseCases.getSetsWithCount().collectLatest { setSets(it) }
            }
        }
    }

    private suspend fun setSets(list: List<SetWithCardsModel>) {
        withContext(provider.main()) {
            uiState = uiState.copy(
                isLoading = false,
                sets = list
            )
        }
    }

    private suspend fun showLoading() {
        withContext(provider.main()) {
            uiState = uiState.copy(isLoading = true)
        }
    }

    private fun deleteSet(setId: Long?) {
        setId ?: return

        viewModelScope.launch {
            withContext(provider.io()) {
                setsUseCases.deleteSet(setId).collectLatest { getSets() }
            }
        }
    }
}