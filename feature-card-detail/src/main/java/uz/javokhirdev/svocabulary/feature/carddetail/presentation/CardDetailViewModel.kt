package uz.javokhirdev.svocabulary.feature.carddetail.presentation

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
import uz.javokhirdev.svocabulary.core.data.extensions.orNullId
import uz.javokhirdev.svocabulary.core.designsystem.icon.VocabIcons
import uz.javokhirdev.svocabulary.core.model.CardModel
import uz.javokhirdev.svocabulary.core.ui.R
import uz.javokhirdev.svocabulary.feature.cards.domain.usecase.CardsUseCases
import javax.inject.Inject

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    private val cardsUseCases: CardsUseCases,
    private val provider: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf(CardDetailState())
        private set

    private val setId = savedStateHandle.get<Long>(Extras.SET_ID).orNotId()
    private val cardId = savedStateHandle.get<Long>(Extras.CARD_ID).orNotId()

    init {
        getCardById()
    }

    fun handleEvent(event: CardDetailEvent) {
        when (event) {
            is CardDetailEvent.TermChanged -> updateTerm(event.term)
            is CardDetailEvent.DefinitionChanged -> updateDefinition(event.definition)
            CardDetailEvent.SaveClick -> upsertSet()
        }
    }

    private fun updateTerm(term: String) {
        updateIsButtonEnabled(
            term = term,
            definition = uiState.definition
        )
    }

    private fun updateDefinition(definition: String) {
        updateIsButtonEnabled(
            term = uiState.term,
            definition = definition
        )
    }

    private fun updateIsButtonEnabled(
        term: String? = null,
        definition: String? = null
    ) {
        uiState = uiState.copy(
            term = term,
            definition = definition,
            isButtonEnabled = !term.isNullOrEmpty() && !definition.isNullOrEmpty(),
        )
    }

    private fun getCardById() {
        viewModelScope.launch {
            withContext(provider.io()) {
                cardsUseCases.getCardById(cardId).collectLatest { model ->
                    val isUpdate = model.id != null
                    val term = model.term.orEmpty()
                    val definition = model.definition.orEmpty()
                    val resources = if (isUpdate) {
                        CardDetailResouces(
                            toolbarTitle = R.string.edit_card,
                            buttonText = R.string.edit,
                            buttonLeadingIcon = VocabIcons.Edit
                        )
                    } else {
                        CardDetailResouces(
                            toolbarTitle = R.string.create_card,
                            buttonText = R.string.save,
                            buttonLeadingIcon = VocabIcons.Save
                        )
                    }

                    setCardData(
                        term = term,
                        definition = definition,
                        resources = resources
                    )
                }
            }
        }
    }

    private suspend fun setCardData(
        term: String,
        definition: String,
        resources: CardDetailResouces
    ) {
        withContext(provider.main()) {
            uiState = uiState.copy(
                term = term,
                definition = definition,
                isButtonEnabled = term.isNotEmpty() && definition.isNotEmpty(),
                resources = resources
            )
        }
    }

    private fun upsertSet() {
        viewModelScope.launch {
            showLoading()

            val model = CardModel(
                id = cardId.orNullId(),
                setId = setId.orNullId(),
                term = uiState.term,
                definition = uiState.definition
            )

            withContext(provider.io()) {
                cardsUseCases.upsertCard(model).collectLatest { setIsSuccess(it) }
            }
        }
    }

    private suspend fun setIsSuccess(isSuccess: Boolean) {
        withContext(provider.main()) {
            uiState = uiState.copy(
                isLoading = false,
                isSuccess = isSuccess
            )
        }
    }

    private suspend fun showLoading() {
        withContext(provider.main()) {
            uiState = uiState.copy(isLoading = true)
        }
    }
}