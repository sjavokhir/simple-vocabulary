package uz.javokhirdev.svocabulary.feature.sets.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.javokhirdev.svocabulary.core.data.DispatcherProvider
import uz.javokhirdev.svocabulary.feature.sets.domain.usecase.SetsUseCases
import javax.inject.Inject

@HiltViewModel
class SetsViewModel @Inject constructor(
    private val setsUseCases: SetsUseCases,
    private val provider: DispatcherProvider
) : ViewModel() {

    val uiState = MutableStateFlow(SetsState())

    init {
        getSets()
    }

    fun handleEvent(event: SetsEvent) {
        when (event) {
            is SetsEvent.OnDeleteClick -> deleteSet(event.setId)
        }
    }

    private fun getSets() {
        uiState.value = uiState.value.copy(
            isLoading = true
        )

        viewModelScope.launch(provider.io()) {
            setsUseCases.getSetsWithCount().collectLatest {
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    sets = it
                )
            }
        }
    }

    private fun deleteSet(setId: Long?) {
        setId ?: return

        viewModelScope.launch(provider.io()) {
            setsUseCases.deleteSet(setId).collectLatest { if (it) getSets() }
        }
    }
}