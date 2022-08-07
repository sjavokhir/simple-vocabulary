package uz.javokhirdev.svocabulary.feature.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uz.javokhirdev.svocabulary.core.data.extensions.sendMail
import uz.javokhirdev.svocabulary.core.data.extensions.shareApp
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabDialog
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabGradientBackground
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabTopAppBar
import uz.javokhirdev.svocabulary.core.designsystem.icon.VocabIcons
import uz.javokhirdev.svocabulary.core.designsystem.theme.LocalSpacing
import uz.javokhirdev.svocabulary.core.ui.R

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val spacing = LocalSpacing.current
    val uiState = viewModel.uiState

    if (uiState.isOpenResetDialog) {
        VocabDialog(
            title = stringResource(id = R.string.reset_title),
            text = stringResource(id = R.string.reset_description),
            positiveText = stringResource(id = R.string.reset),
            negativeText = stringResource(id = R.string.cancel),
            onConfirmClick = {
                viewModel.handleEvent(SettingsEvent.ResetClick)
            },
            onDismissClick = {
                viewModel.handleEvent(SettingsEvent.OnResetDialog(false))
            }
        )
    }

    VocabGradientBackground {
        Scaffold(
            topBar = {
                VocabTopAppBar(
                    title = stringResource(id = R.string.settings),
                    navigationIcon = VocabIcons.ArrowBack,
                    onNavigationClick = onBackClick,
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                    )
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumedWindowInsets(innerPadding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(spacing.normal)
                ) {
                    SettingsItem(
                        onClick = { viewModel.handleEvent(SettingsEvent.ImportClick) },
                        text = stringResource(id = R.string.import_data),
                        leadingIcon = VocabIcons.Import,
                        topEnd = spacing.small,
                        topStart = spacing.small
                    )
                    SettingsItem(
                        onClick = { viewModel.handleEvent(SettingsEvent.ExportClick) },
                        text = stringResource(id = R.string.export_data),
                        leadingIcon = VocabIcons.Export,
                        bottomEnd = spacing.small,
                        bottomStart = spacing.small
                    )
                    Spacer(Modifier.height(spacing.normal))
                    SettingsItem(
                        onClick = {
                            viewModel.handleEvent(SettingsEvent.OnResetDialog(true))
                        },
                        text = stringResource(id = R.string.reset_progress),
                        leadingIcon = VocabIcons.Refresh,
                        topEnd = spacing.small,
                        topStart = spacing.small,
                        bottomEnd = spacing.small,
                        bottomStart = spacing.small
                    )
                    Spacer(Modifier.height(spacing.normal))
                    SettingsItem(
                        onClick = { context.sendMail() },
                        text = stringResource(id = R.string.contact_developer),
                        leadingIcon = VocabIcons.Code,
                        topEnd = spacing.small,
                        topStart = spacing.small,
                    )
                    SettingsItem(
                        onClick = { context.shareApp() },
                        text = stringResource(id = R.string.share_application),
                        leadingIcon = VocabIcons.Share,
                        bottomEnd = spacing.small,
                        bottomStart = spacing.small
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    leadingIcon: ImageVector,
    topStart: Dp = 0.dp,
    topEnd: Dp = 0.dp,
    bottomEnd: Dp = 0.dp,
    bottomStart: Dp = 0.dp
) {
    val spacing = LocalSpacing.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = topStart,
                    topEnd = topEnd,
                    bottomEnd = bottomEnd,
                    bottomStart = bottomStart,
                )
            )
            .background(color = MaterialTheme.colorScheme.surface)
            .clickable { onClick() }
            .padding(spacing.normal)
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = text,
        )
        Spacer(Modifier.width(spacing.normal))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}
