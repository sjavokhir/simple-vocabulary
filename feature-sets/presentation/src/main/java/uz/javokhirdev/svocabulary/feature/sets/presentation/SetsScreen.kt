package uz.javokhirdev.svocabulary.feature.sets.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import uz.javokhirdev.svocabulary.core.ui.isScrollingUp

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun SetsScreen(
    viewModel: SetsViewModel = hiltViewModel(),
    onSettingsClick: () -> Unit,
    onSetClick: (Long?) -> Unit,
    onAddSetClick: (Long?) -> Unit,
) {
    val uiState = viewModel.uiState
    val spacing = LocalSpacing.current
    val clipboard = LocalClipboardManager.current
    val listState = rememberLazyListState()

    uiState.lastLongClickedSetModel?.let {
        VocabActionSheet(
            onDismissClick = { viewModel.handleEvent(SetsEvent.SetLongClick()) },
            onCopyClick = {
                clipboard.setText(AnnotatedString(it.title.orEmpty()))
                viewModel.handleEvent(SetsEvent.SetLongClick())
            },
            onEditClick = {
                onAddSetClick(it.id)
                viewModel.handleEvent(SetsEvent.SetLongClick())
            },
            onDeleteClick = {
                viewModel.handleEvent(SetsEvent.SetDeleteClick(it.id))
                viewModel.handleEvent(SetsEvent.SetLongClick())
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
                } else if (uiState.sets.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState
                    ) {
                        item { Spacer(Modifier.height(spacing.small)) }
                        items(uiState.sets) {
                            SetItem(
                                model = it,
                                onSetClick = onSetClick,
                                onSetLongClick = { model ->
                                    viewModel.handleEvent(SetsEvent.SetLongClick(model))
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
private fun SetItem(
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
            Spacer(Modifier.height(spacing.extraSmall))
            Row(horizontalArrangement = Arrangement.End) {
                Text(
                    text = model.set?.description.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(spacing.normal))
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