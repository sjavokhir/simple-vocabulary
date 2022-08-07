package uz.javokhirdev.svocabulary.feature.setdetail.presentation

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
import uz.javokhirdev.svocabulary.core.model.SetModel
import uz.javokhirdev.svocabulary.core.ui.R
import uz.javokhirdev.svocabulary.feature.sets.domain.usecase.SetsUseCases
import javax.inject.Inject

@HiltViewModel
class SetDetailViewModel @Inject constructor(
    private val setsUseCases: SetsUseCases,
    private val provider: DispatcherProvider,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var uiState by mutableStateOf(SetDetailState())
        private set

    private val setId = savedStateHandle.get<Long>(Extras.SET_ID).orNotId()

    init {
        getSetById()
    }

    fun handleEvent(event: SetDetailEvent) {
        when (event) {
            is SetDetailEvent.TitleChanged -> updateTitle(event.title)
            is SetDetailEvent.DescriptionChanged -> updateDescription(event.description)
            SetDetailEvent.SaveClick -> upsertSet()
        }
    }

    private fun updateTitle(title: String) {
        updateIsButtonEnabled(
            title = title,
            description = uiState.description
        )
    }

    private fun updateDescription(description: String) {
        updateIsButtonEnabled(
            title = uiState.title,
            description = description
        )
    }

    private fun updateIsButtonEnabled(
        title: String? = null,
        description: String? = null
    ) {
        uiState = uiState.copy(
            title = title,
            description = description,
            isButtonEnabled = !title.isNullOrEmpty(),
        )
    }

    private fun getSetById() {
        viewModelScope.launch {
            withContext(provider.io()) {
                setsUseCases.getSetById(setId).collectLatest {
                    val isUpdate = it.id != null
                    val title = it.title.orEmpty()
                    val description = it.description.orEmpty()
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

                    setSetData(
                        title = title,
                        description = description,
                        resources = resources
                    )
                }
            }
        }
    }

    private suspend fun setSetData(
        title: String,
        description: String,
        resources: SetDetailResouces
    ) {
        withContext(provider.main()) {
            uiState = uiState.copy(
                title = title,
                description = description,
                isButtonEnabled = title.isNotEmpty() && description.isNotEmpty(),
                resources = resources
            )
        }
    }

    private fun upsertSet() {
        viewModelScope.launch {
            showLoading()

            val model = SetModel(
                id = setId.orNullId(),
                title = uiState.title,
                description = uiState.description
            )

            withContext(provider.io()) {
                setsUseCases.upsertSet(model).collectLatest { setIsSuccess(it) }
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