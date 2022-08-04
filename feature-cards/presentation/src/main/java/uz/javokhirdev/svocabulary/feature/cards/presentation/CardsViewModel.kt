package uz.javokhirdev.svocabulary.feature.cards.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.javokhirdev.svocabulary.core.data.DispatcherProvider
import uz.javokhirdev.svocabulary.core.data.Extras
import uz.javokhirdev.svocabulary.core.data.extensions.orNotId
import uz.javokhirdev.svocabulary.feature.cards.domain.usecase.CardsUseCases
import uz.javokhirdev.svocabulary.feature.sets.domain.usecase.SetsUseCases
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val cardsUseCases: CardsUseCases,
    private val setsUseCases: SetsUseCases,
    private val provider: DispatcherProvider,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val uiState = MutableStateFlow(CardsState())

    val setId = savedStateHandle.get<Long>(Extras.SET_ID).orNotId()

    init {
        getSetById()
        getCards()
    }

    fun handleEvent(event: CardsEvent) {
        when (event) {
            CardsEvent.OnClearAllClick -> clearAll()
        }
    }

    private fun getSetById() {
        viewModelScope.launch(provider.io()) {
            setsUseCases.getSetById(setId).collectLatest {
                uiState.value = uiState.value.copy(
                    set = it
                )
            }
        }
    }

    private fun getCards() {
        uiState.value = uiState.value.copy(
            isLoading = true
        )

        viewModelScope.launch(provider.io()) {
            cardsUseCases.getCards(setId).collectLatest {
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    cards = it
                )
            }
        }
    }

    fun deleteCard(cardId: Long?) {
        cardId ?: return

        viewModelScope.launch(provider.io()) {
            cardsUseCases.deleteCard(cardId).collectLatest {
                if (it) getCards()
            }
        }
    }

    private fun clearAll() {
        uiState.value = uiState.value.copy(
            isLoading = true
        )

        viewModelScope.launch(provider.io()) {
            cardsUseCases.deleteCardsBySetId(setId).collectLatest {
                if (it) getCards()
            }
        }
    }
}