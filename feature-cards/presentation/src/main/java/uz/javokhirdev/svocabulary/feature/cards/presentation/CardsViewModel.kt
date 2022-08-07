package uz.javokhirdev.svocabulary.feature.cards.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.javokhirdev.svocabulary.core.data.DispatcherProvider
import uz.javokhirdev.svocabulary.core.data.Extras
import uz.javokhirdev.svocabulary.core.data.extensions.orNotId
import uz.javokhirdev.svocabulary.core.model.CardModel
import uz.javokhirdev.svocabulary.core.model.SetModel
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

    var uiState by mutableStateOf(CardsState())
        private set

    val setId = savedStateHandle.get<Long>(Extras.SET_ID).orNotId()

    private var getCardsJob: Job? = null

    init {
        getSetById()
        getCards()
    }

    fun handleEvent(event: CardsEvent) {
        when (event) {
            is CardsEvent.CardLongClick -> {
                uiState = uiState.copy(lastLongClickedCardModel = event.cardModel)
            }
            is CardsEvent.CardDeleteClick -> deleteCard(event.cardId)
            CardsEvent.ClearAllClick -> clearAll()
            is CardsEvent.OnClearAllDialog -> {
                uiState = uiState.copy(isOpenClearAllDialog = event.isOpen)
            }
        }
    }

    private fun getSetById() {
        viewModelScope.launch {
            withContext(provider.io()) {
                setsUseCases.getSetById(setId).collectLatest { setSetData(it) }
            }
        }
    }

    private suspend fun setSetData(setModel: SetModel) {
        withContext(provider.main()) {
            uiState = uiState.copy(set = setModel)
        }
    }

    private fun getCards() {
        getCardsJob?.cancel()
        getCardsJob = viewModelScope.launch {
            showLoading()

            withContext(provider.io()) {
                cardsUseCases.getCards(setId).collectLatest { setCards(it) }
            }
        }
    }

    private suspend fun setCards(list: List<CardModel>) {
        withContext(provider.main()) {
            uiState = uiState.copy(
                isLoading = false,
                cards = list
            )
        }
    }

    private suspend fun showLoading() {
        withContext(provider.main()) {
            uiState = uiState.copy(
                isLoading = true,
                isOpenClearAllDialog = false
            )
        }
    }

    private fun deleteCard(cardId: Long?) {
        cardId ?: return

        viewModelScope.launch {
            withContext(provider.io()) {
                cardsUseCases.deleteCard(cardId).collectLatest { getCards() }
            }
        }
    }

    private fun clearAll() {
        viewModelScope.launch {
            showLoading()

            withContext(provider.io()) {
                cardsUseCases.deleteCardsBySetId(setId).collectLatest { getCards() }
            }
        }
    }
}