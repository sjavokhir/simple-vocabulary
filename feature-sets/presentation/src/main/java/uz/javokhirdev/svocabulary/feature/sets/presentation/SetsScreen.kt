package uz.javokhirdev.svocabulary.feature.sets.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabExtendedFloatingActionButton
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabGradientBackground
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabLoadingWheel
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabTopAppBar
import uz.javokhirdev.svocabulary.core.designsystem.icon.VocabIcons
import uz.javokhirdev.svocabulary.core.designsystem.theme.LocalSpacing
import uz.javokhirdev.svocabulary.core.model.SetModel
import uz.javokhirdev.svocabulary.core.ui.R

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun SetsScreen(
    modifier: Modifier = Modifier,
    viewModel: SetsViewModel = hiltViewModel(),
    onSettingsClick: () -> Unit,
    onAddSetClick: (Long?) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value

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
                    onClick = { onAddSetClick(null) },
                    text = stringResource(id = R.string.add_set),
                    leadingIcon = VocabIcons.Add,
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                    )
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumedWindowInsets(innerPadding)
            ) {
                if (uiState.isLoading) {
                    VocabLoadingWheel(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(uiState.sets) {
                            SetItem(
                                model = it,
                                onAddSetClick = onAddSetClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun SetItem(
    model: SetModel,
    onAddSetClick: (Long?) -> Unit
) {
    val spacing = LocalSpacing.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = spacing.normal,
                vertical = spacing.small
            ),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = spacing.stroke,
            color = Color.Gray.copy(alpha = 0.25f)
        ),
        onClick = { onAddSetClick(model.id) }
    ) {
        Column(modifier = Modifier.padding(spacing.normal)) {
            Text(
                text = model.title.orEmpty(),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(spacing.small))
            Text(
                text = model.description.orEmpty(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
