package uz.javokhirdev.svocabulary.feature.cards.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun CardsScreen(
    modifier: Modifier = Modifier,
    viewModel: CardsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onAddCardClick: (setId: Long?, cardId: Long?) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value
    val spacing = LocalSpacing.current
    val clipboard = LocalClipboardManager.current
    val listState = rememberLazyListState()
    val isClearAll = remember { mutableStateOf(false) }
    val lastCardModel = remember { mutableStateOf(CardModel()) }

    if (lastCardModel.value.id != null) {
        VocabActionSheet(
            onDismissClick = { lastCardModel.value = CardModel() },
            onCopyClick = {
                clipboard.setText(
                    AnnotatedString(
                        "${lastCardModel.value.term.orEmpty()} - ${lastCardModel.value.definition.orEmpty()}"
                    )
                )
                lastCardModel.value = CardModel()
            },
            onListenClick = {},
            onEditClick = {
                onAddCardClick(viewModel.setId, lastCardModel.value.id)
                lastCardModel.value = CardModel()
            },
            onDeleteClick = {
                viewModel.handleEvent(CardsEvent.OnDeleteClick(lastCardModel.value.id))
                lastCardModel.value = CardModel()
            }
        )
    }

    if (isClearAll.value) {
        VocabDialog(
            title = stringResource(id = R.string.clear_all),
            text = stringResource(id = R.string.clear_all_description),
            positiveText = stringResource(id = R.string.delete),
            negativeText = stringResource(id = R.string.cancel),
            onConfirmClick = {
                viewModel.handleEvent(CardsEvent.OnClearAllClick)
                isClearAll.value = false
            },
            onDismissClick = { isClearAll.value = false }
        )
    }

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
                        IconButton(onClick = { isClearAll.value = true }) {
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
                            onClick = { },
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
                                onCardClick = {},
                                onCardLongClick = { model -> lastCardModel.value = model }
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
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun CardItem(
    model: CardModel,
    onCardClick: (Long?) -> Unit,
    onCardLongClick: (CardModel) -> Unit,
) {
    val spacing = LocalSpacing.current

    Column(
        modifier = Modifier
            .padding(vertical = spacing.small)
            .combinedClickable(
                onClick = { onCardClick(model.id) },
                onLongClick = { onCardLongClick(model) },
            )
    ) {
        Divider(
            color = Color.LightGray.copy(alpha = 0.25f),
            thickness = spacing.stroke
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
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

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}
