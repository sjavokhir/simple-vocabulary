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
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Spread
import nl.dionsegijn.konfetti.core.emitter.Emitter
import uz.javokhirdev.svocabulary.core.data.DispatcherProvider
import uz.javokhirdev.svocabulary.core.data.Extras
import uz.javokhirdev.svocabulary.core.data.extensions.orNotId
import uz.javokhirdev.svocabulary.core.model.CardModel
import uz.javokhirdev.svocabulary.feature.cards.domain.usecase.CardsUseCases
import uz.javokhirdev.svocabulary.feature.flashcards.presentation.components.SwipedOutDirection
import java.util.concurrent.TimeUnit
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
            FlashcardsEvent.SideClick -> changeSide()
            is FlashcardsEvent.RemoveCard -> removeCard(event.item, event.direction)
            FlashcardsEvent.Empty -> onEmpty()
            is FlashcardsEvent.OnTipsDialog -> {
                uiState = uiState.copy(isOpenTipsDialog = event.isOpen)
            }
        }
    }

    private fun changeSide() {
        uiState = uiState.copy(isFrontSide = !uiState.isFrontSide)
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

    fun rain(): List<Party> {
        return listOf(
            Party(
                speed = 0f,
                maxSpeed = 15f,
                damping = 0.9f,
                angle = Angle.BOTTOM,
                spread = Spread.ROUND,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                emitter = Emitter(duration = 5, TimeUnit.SECONDS).perSecond(100),
                position = Position.Relative(0.0, 0.0).between(Position.Relative(1.0, 0.0))
            )
        )
    }
}