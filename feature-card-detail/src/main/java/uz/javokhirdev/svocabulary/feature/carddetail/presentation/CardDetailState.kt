package uz.javokhirdev.svocabulary.feature.carddetail.presentation

import androidx.compose.ui.graphics.vector.ImageVector
import uz.javokhirdev.svocabulary.core.designsystem.icon.VocabIcons
import uz.javokhirdev.svocabulary.core.ui.R

data class CardDetailState(
    val term: String? = null,
    val definition: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isButtonEnabled: Boolean = false,
    val resources: CardDetailResouces = CardDetailResouces()
)

data class CardDetailResouces(
    val toolbarTitle: Int = R.string.create_card,
    val buttonText: Int = R.string.save,
    val buttonLeadingIcon: ImageVector = VocabIcons.Save
)