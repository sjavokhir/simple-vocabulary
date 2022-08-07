package uz.javokhirdev.svocabulary.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import uz.javokhirdev.svocabulary.core.designsystem.icon.VocabIcons
import uz.javokhirdev.svocabulary.core.designsystem.theme.LocalSpacing
import uz.javokhirdev.svocabulary.core.ui.R

@Composable
fun VocabActionSheet(
    modifier: Modifier = Modifier,
    onDismissClick: () -> Unit,
    onCopyClick: (() -> Unit)? = null,
    onListenClick: (() -> Unit)? = null,
    onEditClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null,
) {
    Dialog(onDismissRequest = onDismissClick) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (onCopyClick != null) {
                    ActionItem(
                        text = stringResource(id = R.string.copy),
                        leadingIcon = VocabIcons.Copy,
                        onClick = onCopyClick
                    )
                }
                if (onListenClick != null) {
                    ActionItem(
                        text = stringResource(id = R.string.listen),
                        leadingIcon = VocabIcons.VolumeUp,
                        onClick = onListenClick
                    )
                }
                if (onEditClick != null) {
                    ActionItem(
                        text = stringResource(id = R.string.edit),
                        leadingIcon = VocabIcons.Edit,
                        onClick = onEditClick
                    )
                }
                if (onDeleteClick != null) {
                    ActionItem(
                        text = stringResource(id = R.string.delete),
                        leadingIcon = VocabIcons.Delete,
                        onClick = onDeleteClick,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun ActionItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    leadingIcon: ImageVector,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    val spacing = LocalSpacing.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(spacing.normal)
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = text,
            tint = color
        )
        Spacer(Modifier.width(spacing.normal))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = color
        )
    }
}