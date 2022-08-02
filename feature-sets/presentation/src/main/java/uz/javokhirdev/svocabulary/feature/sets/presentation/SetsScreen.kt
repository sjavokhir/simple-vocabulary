package uz.javokhirdev.svocabulary.feature.sets.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
@ExperimentalMaterialApi
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
    val listState = rememberLazyListState()

    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val sheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val sheetCoroutineScope = rememberCoroutineScope()

    VocabActionSheet(
        scaffoldState = sheetScaffoldState,
        modifier = Modifier.windowInsetsPadding(
            WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
        )
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
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        closeBottomSheet(
                            sheetCoroutineScope = sheetCoroutineScope,
                            sheetState = sheetState
                        )
                    })
                }
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
                                    onSetLongClick = {
                                        openBottomSheet(
                                            sheetCoroutineScope = sheetCoroutineScope,
                                            sheetState = sheetState
                                        )
                                    }
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
}

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun SetItem(
    model: SetWithCardsModel,
    onSetClick: (Long?) -> Unit,
    onSetLongClick: (SetModel?) -> Unit
) {
    val spacing = LocalSpacing.current

    Surface(
        modifier = Modifier
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
                onLongClick = { onSetLongClick(model.set) },
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
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(spacing.extraSmall))
            Row(horizontalArrangement = Arrangement.End) {
                Text(
                    text = model.set?.description.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(spacing.normal))
                Text(
                    text = model.cardsCount.orZero().toString(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }
        }
    }
}

@ExperimentalMaterialApi
private fun openBottomSheet(
    sheetCoroutineScope: CoroutineScope,
    sheetState: BottomSheetState
) {
    sheetCoroutineScope.launch {
        if (sheetState.isCollapsed) {
            sheetState.expand()
        } else {
            sheetState.collapse()
        }
    }
}

@ExperimentalMaterialApi
private fun closeBottomSheet(
    sheetCoroutineScope: CoroutineScope,
    sheetState: BottomSheetState
) {
    sheetCoroutineScope.launch {
        sheetState.collapse()
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