package uz.javokhirdev.svocabulary.feature.cards.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import uz.javokhirdev.svocabulary.core.designsystem.component.*
import uz.javokhirdev.svocabulary.core.designsystem.icon.VocabIcons
import uz.javokhirdev.svocabulary.core.designsystem.theme.LocalSpacing
import uz.javokhirdev.svocabulary.core.model.CardModel
import uz.javokhirdev.svocabulary.core.ui.R
import uz.javokhirdev.svocabulary.core.ui.isScrollingUp
import uz.javokhirdev.svocabulary.core.ui.rememberTtsController

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun CardsScreen(
    viewModel: CardsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onAddCardClick: (setId: Long?, cardId: Long?) -> Unit,
    onFlashcardsClick: (setId: Long?) -> Unit
) {
    val uiState = viewModel.uiState
    val spacing = LocalSpacing.current
    val clipboard = LocalClipboardManager.current
    val listState = rememberLazyListState()
    val ttsController = rememberTtsController()

    uiState.lastLongClickedCardModel?.let {
        VocabActionSheet(
            onDismissClick = {
                viewModel.handleEvent(CardsEvent.CardLongClick())
            },
            onCopyClick = {
                clipboard.setText(
                    AnnotatedString("${it.term} - ${it.definition}")
                )
                viewModel.handleEvent(CardsEvent.CardLongClick())
            },
            onListenClick = {
                ttsController.say("${it.term} - ${it.definition}")
                viewModel.handleEvent(CardsEvent.CardLongClick())
            },
            onEditClick = {
                onAddCardClick(viewModel.setId, it.id)
                viewModel.handleEvent(CardsEvent.CardLongClick())
            },
            onDeleteClick = {
                viewModel.handleEvent(CardsEvent.CardDeleteClick(it.id))
                viewModel.handleEvent(CardsEvent.CardLongClick())
            }
        )
    }

    if (uiState.isOpenClearAllDialog) {
        VocabDialog(
            title = stringResource(id = R.string.clear_all),
            text = stringResource(id = R.string.clear_all_description),
            positiveText = stringResource(id = R.string.delete),
            negativeText = stringResource(id = R.string.cancel),
            onConfirmClick = {
                viewModel.handleEvent(CardsEvent.ClearAllClick)
            },
            onDismissClick = {
                viewModel.handleEvent(CardsEvent.OnClearAllDialog(false))
            }
        )
    }

    VocabGradientBackground {
        Scaffold(
            topBar = {
                VocabTopAppBar(
                    title = "",
                    navigationIcon = VocabIcons.ArrowBack,
                    onNavigationClick = {
                        ttsController.shutDown()
                        onBackClick()
                    },
                    actions = {
                        IconButton(onClick = {
                            ttsController.shutDown()
                            onAddCardClick(viewModel.setId, null)
                        }) {
                            Icon(
                                imageVector = VocabIcons.Add,
                                contentDescription = stringResource(id = R.string.add_card),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(onClick = {
                            viewModel.handleEvent(CardsEvent.OnClearAllDialog(true))
                        }) {
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
            floatingActionButton = {
                if (uiState.cards.isNotEmpty()) {
                    AnimatedVisibility(
                        visible = listState.isScrollingUp(),
                        enter = scaleIn(),
                        exit = scaleOut(),
                    ) {
                        VocabExtendedFloatingActionButton(
                            onClick = {
                                ttsController.shutDown()
                                onFlashcardsClick(viewModel.setId)
                            },
                            text = stringResource(id = R.string.flashcards),
                            leadingIcon = VocabIcons.FitnessCenter,
                            modifier = Modifier.windowInsetsPadding(
                                WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                            )
                        )
                    }
                }
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumedWindowInsets(innerPadding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                    )
            ) {
                if (uiState.isLoading) {
                    VocabLoadingWheel(Modifier.align(Alignment.Center))
                } else if (uiState.cards.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState
                    ) {
                        item { Spacer(Modifier.height(spacing.small)) }
                        items(uiState.cards) {
                            CardItem(
                                model = it,
                                onCardClick = {},
                                onCardLongClick = { model ->
                                    viewModel.handleEvent(CardsEvent.CardLongClick(model))
                                }
                            )
                        }
                        item { Spacer(Modifier.height(spacing.small)) }
                    }
                } else {
                    Text(
                        text = stringResource(id = R.string.no_data),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
private fun CardItem(
    model: CardModel,
    onCardClick: (Long?) -> Unit,
    onCardLongClick: (CardModel) -> Unit,
) {
    val spacing = LocalSpacing.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spacing.small)
            .combinedClickable(
                onClick = { onCardClick(model.id) },
                onLongClick = { onCardLongClick(model) },
            ),
        color = MaterialTheme.colorScheme.surface
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
            Spacer(Modifier.height(spacing.extraSmall))
            Text(
                text = model.definition.orEmpty(),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }
    }
}