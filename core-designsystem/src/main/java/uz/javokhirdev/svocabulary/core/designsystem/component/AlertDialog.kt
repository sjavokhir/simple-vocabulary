package uz.javokhirdev.svocabulary.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.Dialog
import uz.javokhirdev.svocabulary.core.designsystem.theme.LocalSpacing

@Composable
fun VocabDialog(
    title: String,
    text: String,
    positiveText: String,
    negativeText: String,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    val spacing = LocalSpacing.current

    Dialog(onDismissRequest = onDismissClick) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = spacing.medium,
                            vertical = spacing.normal
                        )
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(spacing.extraNormal))
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(
                            end = spacing.extraNormal,
                            bottom = spacing.extraNormal
                        )
                ) {
                    VocabTextButton(
                        onClick = onDismissClick,
                        text = negativeText,
                        small = true,
                        colors = VocabButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    )
                    Spacer(Modifier.width(spacing.small))
                    VocabTextButton(
                        onClick = onConfirmClick,
                        text = positiveText,
                        small = true,
                        colors = VocabButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    }
}