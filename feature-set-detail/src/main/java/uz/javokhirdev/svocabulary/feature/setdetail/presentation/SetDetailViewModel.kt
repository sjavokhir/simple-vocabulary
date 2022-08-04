package uz.javokhirdev.svocabulary.feature.setdetail.presentation

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
import uz.javokhirdev.svocabulary.core.model.SetModel
import uz.javokhirdev.svocabulary.core.ui.R
import uz.javokhirdev.svocabulary.feature.sets.domain.usecase.SetsUseCases
import javax.inject.Inject

@HiltViewModel
class SetDetailViewModel @Inject constructor(
    private val setsUseCases: SetsUseCases,
    savedStateHandle: SavedStateHandle,
    private val provider: DispatcherProvider
) : ViewModel() {

    val uiState = MutableStateFlow(SetDetailState())

    private val setId = savedStateHandle.get<Long>(Extras.SET_ID).orNotId()

    init {
        getSetById()
    }

    fun handleEvent(event: SetDetailEvent) {
        when (event) {
            is SetDetailEvent.TitleChanged -> updateTitle(event.title)
            is SetDetailEvent.DescriptionChanged -> updateDescription(event.description)
            SetDetailEvent.OnSaveClick -> upsertSet()
        }
    }

    private fun updateTitle(title: String) {
        updateIsButtonEnabled(
            title = title,
            description = uiState.value.description
        )
    }

    private fun updateDescription(description: String) {
        updateIsButtonEnabled(
            title = uiState.value.title,
            description = description
        )
    }

    private fun updateIsButtonEnabled(
        title: String? = null,
        description: String? = null
    ) {
        uiState.value = uiState.value.copy(
            title = title,
            description = description,
            isButtonEnabled = !title.isNullOrEmpty() && !description.isNullOrEmpty(),
        )
    }

    private fun getSetById() {
        viewModelScope.launch(provider.io()) {
            setsUseCases.getSetById(setId).collectLatest { model ->
                val isUpdate = model.id != null
                val title = model.title.orEmpty()
                val description = model.description.orEmpty()
                val resources = if (isUpdate) {
                    SetDetailResouces(
                        toolbarTitle = R.string.edit_set,
                        buttonText = R.string.edit,
                        buttonLeadingIcon = VocabIcons.Edit
                    )
                } else {
                    SetDetailResouces(
                        toolbarTitle = R.string.create_set,
                        buttonText = R.string.save,
                        buttonLeadingIcon = VocabIcons.Save
                    )
                }

                uiState.value = uiState.value.copy(
                    title = title,
                    description = description,
                    isButtonEnabled = title.isNotEmpty() && description.isNotEmpty(),
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
            val model = SetModel(
                id = setId.orNullId(),
                title = uiState.value.title,
                description = uiState.value.description
            )

            setsUseCases.upsertSet(model).collectLatest {
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    isSuccess = it
                )
            }
        }
    }
}