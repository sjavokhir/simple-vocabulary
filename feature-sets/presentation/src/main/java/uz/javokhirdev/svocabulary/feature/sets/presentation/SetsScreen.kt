package uz.javokhirdev.svocabulary.feature.sets.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabExtendedFloatingActionButton
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabGradientBackground
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabTopAppBar
import uz.javokhirdev.svocabulary.core.designsystem.icon.VocabIcons
import uz.javokhirdev.svocabulary.core.ui.R

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun SetsRoute(
    modifier: Modifier = Modifier,
    viewModel: SetsViewModel = hiltViewModel(),
    navigateToSettings: () -> Unit,
    navigateToSetDetail: () -> Unit
) {
    SetsScreen(
        modifier = modifier,
        onSettingsClick = navigateToSettings,
        onAddSetClick = navigateToSetDetail
    )
}

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun SetsScreen(
    modifier: Modifier,
    onSettingsClick: () -> Unit,
    onAddSetClick: () -> Unit
) {
    VocabGradientBackground {
        Scaffold(
            topBar = {
                VocabTopAppBar(
                    title = stringResource(id = R.string.study_sets),
                    actions = {
                        IconButton(onClick = onSettingsClick) {
                            Icon(
                                imageVector = VocabIcons.Settings,
                                contentDescription = stringResource(id = R.string.settings),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                    )
                )
            },
            floatingActionButton = {
                VocabExtendedFloatingActionButton(
                    onClick = onAddSetClick,
                    title = R.string.add_set,
                    icon = VocabIcons.Add,
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                    )
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            BoxWithConstraints(
                modifier = modifier
                    .padding(innerPadding)
                    .consumedWindowInsets(innerPadding)
            ) {

            }
        }
    }
}
