package uz.javokhirdev.svocabulary.core.designsystem.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/**
 * Vocab in Android filled button with text and icon content slots.
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param small Whether or not the size of the button should be small or regular.
 * @param colors [ButtonColors] that will be used to resolve the container and content color for
 * this button in different states. See [VocabButtonDefaults.filledButtonColors].
 * @param text The button text label content.
 * @param leadingIcon The button leading icon content. Pass `null` here for no leading icon.
 * @param trailingIcon The button trailing icon content. Pass `null` here for no trailing icon.
 */
@Composable
fun VocabFilledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    small: Boolean = false,
    colors: ButtonColors = VocabButtonDefaults.filledButtonColors(),
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = if (small) {
            modifier.heightIn(min = VocabButtonDefaults.SmallButtonHeight)
        } else {
            modifier.heightIn(min = VocabButtonDefaults.NormalButtonHeight)
        },
        enabled = enabled,
        colors = colors,
        contentPadding = VocabButtonDefaults.buttonContentPadding(
            small = small,
            leadingIcon = leadingIcon != null,
            trailingIcon = trailingIcon != null
        ),
        content = {
            ProvideTextStyle(value = textStyle) {
                VocabButtonContent(
                    text = text,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon
                )
            }
        }
    )
}

/**
 * Vocab in Android text button with text and icon content slots.
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param small Whether or not the size of the button should be small or regular.
 * @param colors [ButtonColors] that will be used to resolve the container and content color for
 * this button in different states. See [VocabButtonDefaults.textButtonColors].
 * @param text The button text label content.
 * @param leadingIcon The button leading icon content. Pass `null` here for no leading icon.
 * @param trailingIcon The button trailing icon content. Pass `null` here for no trailing icon.
 */
@Composable
fun VocabTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    small: Boolean = false,
    colors: ButtonColors = VocabButtonDefaults.textButtonColors(),
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null
) {
    TextButton(
        onClick = onClick,
        modifier = if (small) {
            modifier.heightIn(min = VocabButtonDefaults.SmallButtonHeight)
        } else {
            modifier.heightIn(min = VocabButtonDefaults.NormalButtonHeight)
        },
        enabled = enabled,
        colors = colors,
        contentPadding = VocabButtonDefaults.buttonContentPadding(
            small = small,
            leadingIcon = leadingIcon != null,
            trailingIcon = trailingIcon != null
        ),
        content = {
            ProvideTextStyle(value = textStyle) {
                VocabButtonContent(
                    text = text,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon
                )
            }
        }
    )
}

/**
 * Vocab in Android extended floating action button with text and icon content slots.
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param text The button text label content.
 * @param leadingIcon The button leading icon content. Pass `null` here for no leading icon.
 * @param trailingIcon The button trailing icon content. Pass `null` here for no trailing icon.
 */
@Composable
fun VocabExtendedFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        content = {
            ProvideTextStyle(value = textStyle) {
                VocabButtonContent(
                    text = text,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon
                )
            }
        }
    )
}

/**
 * Internal Vocab in Android button content layout for arranging the text label, leading icon and
 * trailing icon.
 *
 * @param text The button text label content.
 * @param leadingIcon The button leading icon content. Pass `null` here for no leading icon.
 * @param trailingIcon The button trailing icon content. Pass `null` here for no trailing icon.
 */
@Composable
private fun RowScope.VocabButtonContent(
    text: String,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null
) {
    if (leadingIcon != null) {
        Box(Modifier.sizeIn(maxHeight = VocabButtonDefaults.ButtonIconSize)) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = text
            )
        }
    }
    Box(
        Modifier
            .weight(1f, fill = false)
            .padding(
                start = if (leadingIcon != null) {
                    VocabButtonDefaults.ButtonContentSpacing
                } else {
                    0.dp
                },
                end = if (trailingIcon != null) {
                    VocabButtonDefaults.ButtonContentSpacing
                } else {
                    0.dp
                }
            )
    ) {
        Text(text = text)
    }
    if (trailingIcon != null) {
        Box(Modifier.sizeIn(maxHeight = VocabButtonDefaults.ButtonIconSize)) {
            Icon(
                imageVector = trailingIcon,
                contentDescription = text
            )
        }
    }
}

/**
 * Vocab in Android button default values.
 */
object VocabButtonDefaults {
    val SmallButtonHeight = 36.dp
    val NormalButtonHeight = 52.dp
    private const val DisabledButtonContainerAlpha = 0.12f
    private const val DisabledButtonContentAlpha = 0.38f
    private val ButtonHorizontalPadding = 24.dp
    private val SmallButtonHorizontalPadding = 16.dp
    val ButtonContentSpacing = 12.dp
    val ButtonIconSize = 24.dp

    fun buttonContentPadding(
        small: Boolean,
        leadingIcon: Boolean = false,
        trailingIcon: Boolean = false
    ): PaddingValues {
        return PaddingValues(
            start = when {
                small && leadingIcon -> SmallButtonHorizontalPadding
                small -> SmallButtonHorizontalPadding
                else -> ButtonHorizontalPadding
            },
            top = 0.dp,
            end = when {
                small && trailingIcon -> SmallButtonHorizontalPadding
                small -> SmallButtonHorizontalPadding
                else -> ButtonHorizontalPadding
            },
            bottom = 0.dp
        )
    }

    @Composable
    fun filledButtonColors(
        containerColor: Color = MaterialTheme.colorScheme.onBackground,
        contentColor: Color = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor: Color = MaterialTheme.colorScheme.onBackground.copy(
            alpha = DisabledButtonContainerAlpha
        ),
        disabledContentColor: Color = MaterialTheme.colorScheme.onBackground.copy(
            alpha = DisabledButtonContentAlpha
        )
    ) = ButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor
    )

    @Composable
    fun textButtonColors(
        containerColor: Color = Color.Transparent,
        contentColor: Color = MaterialTheme.colorScheme.onBackground,
        disabledContainerColor: Color = Color.Transparent,
        disabledContentColor: Color = MaterialTheme.colorScheme.onBackground.copy(
            alpha = DisabledButtonContentAlpha
        )
    ) = ButtonDefaults.textButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor
    )
}
