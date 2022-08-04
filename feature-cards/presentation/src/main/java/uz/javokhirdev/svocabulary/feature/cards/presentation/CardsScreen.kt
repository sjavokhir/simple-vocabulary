package uz.javokhirdev.svocabulary.feature.cards.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabDialog
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabGradientBackground
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabLoadingWheel
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabTopAppBar
import uz.javokhirdev.svocabulary.core.designsystem.icon.VocabIcons
import uz.javokhirdev.svocabulary.core.designsystem.theme.LocalSpacing
import uz.javokhirdev.svocabulary.core.model.CardModel
import uz.javokhirdev.svocabulary.core.ui.R

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun CardsScreen(
    modifier: Modifier = Modifier,
    viewModel: CardsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onAddCardClick: (Long?, Long?) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value
    val spacing = LocalSpacing.current
    val openDialog = remember { mutableStateOf(false) }

    VocabGradientBackground {
        Scaffold(
            topBar = {
                VocabTopAppBar(
                    title = "",
                    navigationIcon = VocabIcons.ArrowBack,
                    onNavigationClick = onBackClick,
                    actions = {
                        IconButton(onClick = { onAddCardClick(viewModel.setId, null) }) {
                            Icon(
                                imageVector = VocabIcons.Add,
                                contentDescription = stringResource(id = R.string.add_card),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(onClick = { openDialog.value = true }) {
                            Icon(
                                imageVector = VocabIcons.Clear,
                                contentDescription = stringResource(id = R.string.clear_all),
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
                } else if (uiState.cards.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
                    ) {
                        item { Spacer(modifier = Modifier.height(spacing.small)) }
                        items(uiState.cards) {
                            CardItem(
                                model = it,
                                onCardClick = { },
                            )
                        }
                        item { Spacer(modifier = Modifier.height(spacing.small)) }
                    }
                } else {
                    Text(
                        text = stringResource(id = R.string.no_data),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            if (openDialog.value) {
                VocabDialog(
                    title = stringResource(id = R.string.clear_all),
                    text = stringResource(id = R.string.clear_all_description),
                    positiveText = stringResource(id = R.string.delete),
                    negativeText = stringResource(id = R.string.cancel),
                    onConfirmClick = {
                        openDialog.value = false
                        viewModel.handleEvent(CardsEvent.OnClearAllClick)
                    },
                    onDismissClick = {
                        openDialog.value = false
                    }
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun CardItem(
    model: CardModel,
    onCardClick: (Long?) -> Unit
) {
    val spacing = LocalSpacing.current

    Column(modifier = Modifier.padding(vertical = spacing.small)) {
        Divider(
            color = Color.LightGray.copy(alpha = 0.25f),
            thickness = spacing.stroke
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            onClick = { onCardClick(model.id) }
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = spacing.normal,
                        vertical = spacing.extraNormal
                    )
            ) {
                Text(
                    text = model.term.orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(spacing.extraSmall))
                Text(
                    text = model.definition.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        }
        Divider(
            color = Color.LightGray.copy(alpha = 0.25f),
            thickness = spacing.stroke
        )
    }
}
