package uz.javokhirdev.svocabulary.feature.setdetail.presentation

import androidx.compose.ui.graphics.vector.ImageVector
import uz.javokhirdev.svocabulary.core.designsystem.icon.VocabIcons
import uz.javokhirdev.svocabulary.core.ui.R

data class SetDetailState(
    val title: String? = null,
    val description: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isButtonEnabled: Boolean = false,
    val resources: SetDetailResouces = SetDetailResouces()
)

data class SetDetailResouces(
    val toolbarTitle: Int = R.string.create_set,
    val buttonText: Int = R.string.save,
    val buttonLeadingIcon: ImageVector = VocabIcons.Save
)