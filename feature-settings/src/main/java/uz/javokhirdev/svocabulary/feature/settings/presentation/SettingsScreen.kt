package uz.javokhirdev.svocabulary.feature.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val spacing = LocalSpacing.current
    val uiState = viewModel.uiState.collectAsState().value
    val isReset = remember { mutableStateOf(false) }

    if (isReset.value) {
        VocabDialog(
            title = stringResource(id = R.string.reset_title),
            text = stringResource(id = R.string.reset_description),
            positiveText = stringResource(id = R.string.reset),
            negativeText = stringResource(id = R.string.cancel),
            onConfirmClick = {
                viewModel.handleEvent(SettingsEvent.OnResetClick)
                isReset.value = false
            },
            onDismissClick = { isReset.value = false }
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
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumedWindowInsets(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(spacing.normal)
                ) {
                    SettingsItem(
                        onClick = { viewModel.handleEvent(SettingsEvent.OnImportClick) },
                        text = stringResource(id = R.string.import_data),
                        leadingIcon = VocabIcons.Import
                    )
                    Spacer(modifier = Modifier.height(spacing.normal))
                    SettingsItem(
                        onClick = { viewModel.handleEvent(SettingsEvent.OnExportClick) },
                        text = stringResource(id = R.string.export_data),
                        leadingIcon = VocabIcons.Export
                    )
                    Spacer(modifier = Modifier.height(spacing.normal))
                    SettingsItem(
                        onClick = { isReset.value = true },
                        text = stringResource(id = R.string.reset_progress),
                        leadingIcon = VocabIcons.Refresh
                    )
                    Spacer(modifier = Modifier.height(spacing.normal))
                    SettingsItem(
                        onClick = { context.sendMail() },
                        text = stringResource(id = R.string.contact_developer),
                        leadingIcon = VocabIcons.Code
                    )
                    Spacer(modifier = Modifier.height(spacing.normal))
                    SettingsItem(
                        onClick = { context.shareApp() },
                        text = stringResource(id = R.string.share_application),
                        leadingIcon = VocabIcons.Share
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    leadingIcon: ImageVector
) {
    val spacing = LocalSpacing.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .border(
                width = spacing.stroke,
                color = Color.Gray.copy(alpha = 0.25f),
                shape = MaterialTheme.shapes.small
            )
            .background(color = MaterialTheme.colorScheme.surface)
            .clickable { onClick() }
            .padding(spacing.normal)
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = text,
        )
        Spacer(modifier = Modifier.width(spacing.normal))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}
