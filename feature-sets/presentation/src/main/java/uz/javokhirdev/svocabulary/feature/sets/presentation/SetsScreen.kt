package uz.javokhirdev.svocabulary.feature.sets.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import uz.javokhirdev.svocabulary.core.data.extensions.orZero
import uz.javokhirdev.svocabulary.core.designsystem.component.*
import uz.javokhirdev.svocabulary.core.designsystem.icon.VocabIcons
import uz.javokhirdev.svocabulary.core.designsystem.theme.LocalSpacing
import uz.javokhirdev.svocabulary.core.model.SetModel
import uz.javokhirdev.svocabulary.core.model.SetWithCardsModel
import uz.javokhirdev.svocabulary.core.ui.R

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun SetsScreen(
    modifier: Modifier = Modifier,
    viewModel: SetsViewModel = hiltViewModel(),
    onSettingsClick: () -> Unit,
    onSetClick: (Long?) -> Unit,
    onAddSetClick: (Long?) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState().value
    val spacing = LocalSpacing.current
    val clipboard = LocalClipboardManager.current
    val listState = rememberLazyListState()
    val lastSetModel = remember { mutableStateOf(SetModel()) }

    if (lastSetModel.value.id != null) {
        VocabActionSheet(
            onDismissClick = { lastSetModel.value = SetModel() },
            onCopyClick = {
                clipboard.setText(AnnotatedString(lastSetModel.value.title.orEmpty()))
                lastSetModel.value = SetModel()
            },
            onEditClick = {
                onAddSetClick(lastSetModel.value.id)
                lastSetModel.value = SetModel()
            },
            onDeleteClick = {
                viewModel.handleEvent(SetsEvent.OnDeleteClick(lastSetModel.value.id))
                lastSetModel.value = SetModel()
            }
        )
    }

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
                AnimatedVisibility(
                    visible = listState.isScrollingUp(),
                    enter = scaleIn(),
                    exit = scaleOut(),
                ) {
                    VocabExtendedFloatingActionButton(
                        onClick = { onAddSetClick(null) },
                        text = stringResource(id = R.string.add_set),
                        leadingIcon = VocabIcons.Add,
                        modifier = Modifier.windowInsetsPadding(
                            WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                        )
                    )
                }
            },
            containerColor = Color.Transparent,
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
                } else if (uiState.sets.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(
                                WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                            ),
                        state = listState
                    ) {
                        item { Spacer(modifier = Modifier.height(spacing.small)) }
                        items(uiState.sets) {
                            SetItem(
                                model = it,
                                onSetClick = onSetClick,
                                onSetLongClick = { model -> lastSetModel.value = model }
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
fun SetItem(
    model: SetWithCardsModel,
    modifier: Modifier = Modifier,
    onSetClick: (Long?) -> Unit,
    onSetLongClick: (SetModel) -> Unit,
) {
    val spacing = LocalSpacing.current

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = spacing.normal,
                vertical = spacing.small
            )
            .clip(MaterialTheme.shapes.small)
            .border(
                width = spacing.stroke,
                color = Color.Gray.copy(alpha = 0.25f),
                shape = MaterialTheme.shapes.small
            )
            .background(color = MaterialTheme.colorScheme.surface)
            .combinedClickable(
                onClick = { onSetClick(model.set?.id) },
                onLongClick = { model.set?.let { onSetLongClick(it) } },
            )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = spacing.normal,
                vertical = spacing.extraNormal
            )
        ) {
            Text(
                text = model.set?.title.orEmpty(),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(spacing.extraSmall))
            Row(horizontalArrangement = Arrangement.End) {
                Text(
                    text = model.set?.description.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(spacing.normal))
                Text(
                    text = model.cardsCount.orZero().toString(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }
        }
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