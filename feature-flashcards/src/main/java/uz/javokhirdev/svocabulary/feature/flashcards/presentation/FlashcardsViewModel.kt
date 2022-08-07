package uz.javokhirdev.svocabulary.feature.flashcards.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.javokhirdev.svocabulary.core.data.DispatcherProvider
import uz.javokhirdev.svocabulary.core.data.Extras
import uz.javokhirdev.svocabulary.core.data.extensions.orNotId
import uz.javokhirdev.svocabulary.core.model.CardModel
import uz.javokhirdev.svocabulary.feature.cards.domain.usecase.CardsUseCases
import uz.javokhirdev.svocabulary.feature.flashcards.presentation.components.SwipedOutDirection
import javax.inject.Inject

@HiltViewModel
class FlashcardsViewModel @Inject constructor(
    private val cardsUseCases: CardsUseCases,
    private val provider: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf(FlashcardsState())
        private set

    private val setId = savedStateHandle.get<Long>(Extras.SET_ID).orNotId()

    init {
        getCards()
    }

    fun handleEvent(event: FlashcardsEvent) {
        when (event) {
            FlashcardsEvent.InfoClick -> {}
            FlashcardsEvent.OnEmpty -> onEmpty()
            is FlashcardsEvent.RemoveCard -> removeCard(event.item, event.direction)
        }
    }

    private fun getCards() {
        viewModelScope.launch {
            showLoading()

            withContext(provider.io()) {
                cardsUseCases.getCards(setId).collectLatest { setCards(it.shuffled()) }
            }
        }
    }

    private suspend fun setCards(list: List<CardModel>) {
        withContext(provider.main()) {
            uiState = uiState.copy(isLoading = false)
            uiState.cards.clear()
            uiState.cards.addAll(list)
        }
    }

    private suspend fun showLoading() {
        withContext(provider.main()) {
            uiState = uiState.copy(isLoading = true)
        }
    }

    private fun removeCard(item: CardModel, direction: SwipedOutDirection) {
        uiState.cards.remove(item)

        when (direction) {
            SwipedOutDirection.LEFT -> uiState.forgots.add(item)
            SwipedOutDirection.RIGHT -> uiState.knows.add(item)
        }
    }

    private fun onEmpty() {
        viewModelScope.launch {
            if (uiState.forgots.isNotEmpty()) {
                val forgots = uiState.forgots.shuffled()

                uiState.forgots.clear()
                uiState.knows.clear()

                setCards(forgots)
            } else {
                uiState = uiState.copy(isFinished = true)
            }
        }
    }
}