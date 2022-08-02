package uz.javokhirdev.svocabulary.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.javokhirdev.svocabulary.core.designsystem.icon.VocabIcons
import uz.javokhirdev.svocabulary.core.designsystem.theme.LocalSpacing
import uz.javokhirdev.svocabulary.core.ui.R

@ExperimentalMaterialApi
@Composable
fun VocabActionSheet(
    modifier: Modifier = Modifier,
    scaffoldState: BottomSheetScaffoldState,
    content: @Composable (PaddingValues) -> Unit,
) {
    val spacing = LocalSpacing.current

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetElevation = 56.dp,
        sheetShape = RoundedCornerShape(
            topStart = spacing.normal,
            topEnd = spacing.normal
        ),
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = spacing.small)
                    .clip(
                        RoundedCornerShape(
                            topStart = spacing.normal,
                            topEnd = spacing.normal
                        )
                    )
            ) {
                ActionItem(
                    text = stringResource(id = R.string.copy),
                    imageVector = VocabIcons.Copy
                )
                ActionItem(
                    text = stringResource(id = R.string.listen),
                    imageVector = VocabIcons.VolumeUp
                )
                ActionItem(
                    text = stringResource(id = R.string.edit),
                    imageVector = VocabIcons.Edit
                )
                ActionItem(
                    text = stringResource(id = R.string.delete),
                    imageVector = VocabIcons.Delete
                )
            }
        },
        sheetPeekHeight = 0.dp
    ) {
        content(it)
    }
}

@Composable
fun ActionItem(
    text: String,
    imageVector: ImageVector
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(spacing.normal)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(spacing.normal))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}