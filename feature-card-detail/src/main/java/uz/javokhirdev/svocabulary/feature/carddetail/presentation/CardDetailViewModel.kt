package uz.javokhirdev.svocabulary.feature.carddetail.presentation

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

    val uiState = MutableStateFlow(CardDetailState())

    private val setId = savedStateHandle.get<Long>(Extras.SET_ID).orNotId()
    private val cardId = savedStateHandle.get<Long>(Extras.CARD_ID).orNotId()

    init {
        getCardById()
    }

    fun handleEvent(event: CardDetailEvent) {
        when (event) {
            is CardDetailEvent.TermChanged -> updateTerm(event.term)
            is CardDetailEvent.DefinitionChanged -> updateDefinition(event.definition)
            CardDetailEvent.OnSaveClick -> upsertSet()
        }
    }

    private fun updateTerm(term: String) {
        updateIsButtonEnabled(
            term = term,
            definition = uiState.value.definition
        )
    }

    private fun updateDefinition(definition: String) {
        updateIsButtonEnabled(
            term = uiState.value.term,
            definition = definition
        )
    }

    private fun updateIsButtonEnabled(
        term: String? = null,
        definition: String? = null
    ) {
        uiState.value = uiState.value.copy(
            term = term,
            definition = definition,
            isButtonEnabled = !term.isNullOrEmpty() && !definition.isNullOrEmpty(),
        )
    }

    private fun getCardById() {
        viewModelScope.launch(provider.io()) {
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

                uiState.value = uiState.value.copy(
                    term = term,
                    definition = definition,
                    isButtonEnabled = term.isNotEmpty() && definition.isNotEmpty(),
                    resources = resources
                )
            }
        }
    }

    private fun upsertSet() {
        uiState.value = uiState.value.copy(
            isLoading = true
        )

        viewModelScope.launch(provider.io()) {
            val model = CardModel(
                id = cardId.orNullId(),
                setId = setId.orNullId(),
                term = uiState.value.term,
                definition = uiState.value.definition
            )

            cardsUseCases.upsertCard(model).collectLatest {
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    isSuccess = it
                )
            }
        }
    }
}