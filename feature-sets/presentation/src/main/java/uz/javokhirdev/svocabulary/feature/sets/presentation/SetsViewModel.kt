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

    private fun getSets() {
        setLoading()

        viewModelScope.launch(provider.io()) {
            setsUseCases.getSets().collectLatest {
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    sets = it
                )
            }
        }
    }

    private fun setLoading() {
        uiState.value = uiState.value.copy(
            isLoading = true
        )
    }
}