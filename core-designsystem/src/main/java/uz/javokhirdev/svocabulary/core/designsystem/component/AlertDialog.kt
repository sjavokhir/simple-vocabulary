package uz.javokhirdev.svocabulary.core.designsystem.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun VocabDialog(
    title: String,
    text: String,
    positiveText: String,
    negativeText: String,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
) {
    AlertDialog(
        // Dismiss the dialog when the user clicks outside the dialog or on the back
        // button. If you want to disable that functionality, simply use an empty
        // onDismissRequest.
        onDismissRequest = onDismissClick,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        confirmButton = {
            VocabTextButton(
                onClick = onDismissClick,
                text = negativeText,
                small = true
            )
        },
        dismissButton = {
            VocabTextButton(
                onClick = onConfirmClick,
                text = positiveText,
                small = true
            )
        }
    )
}