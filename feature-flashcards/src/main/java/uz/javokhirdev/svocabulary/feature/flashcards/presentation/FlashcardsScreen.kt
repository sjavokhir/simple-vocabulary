package uz.javokhirdev.svocabulary.feature.flashcards.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabGradientBackground
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabLoadingWheel
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabTopAppBar
import uz.javokhirdev.svocabulary.core.designsystem.icon.VocabIcons
import uz.javokhirdev.svocabulary.core.designsystem.theme.LocalSpacing
import uz.javokhirdev.svocabulary.core.ui.R
import uz.javokhirdev.svocabulary.core.ui.rememberTtsController
import uz.javokhirdev.svocabulary.feature.flashcards.presentation.components.Swiper
import uz.javokhirdev.svocabulary.feature.flashcards.presentation.components.rememberSwiperFlipController
import uz.javokhirdev.svocabulary.feature.flashcards.presentation.model.QuizTipModel

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun FlashcardsScreen(
    viewModel: FlashcardsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState = viewModel.uiState
    val ttsController = rememberTtsController()

    if (uiState.isOpenTipsDialog) {
        Dialog(onDismissRequest = {
            viewModel.handleEvent(FlashcardsEvent.OnTipsDialog(false))
        }) {
            QuizTipsContent()
        }
    }

    VocabGradientBackground {
        Scaffold(
            topBar = {
                VocabTopAppBar(
                    title = "",
                    navigationIcon = VocabIcons.Close,
                    onNavigationClick = {
                        ttsController.shutDown()
                        onBackClick()
                    },
                    actions = {
                        IconButton(onClick = {
                            viewModel.handleEvent(FlashcardsEvent.OnTipsDialog(true))
                        }) {
                            Icon(
                                imageVector = VocabIcons.Info,
                                contentDescription = stringResource(id = R.string.quiz_tips),
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
            if (uiState.isFinished) {
                ttsController.shutDown()
                KonfettiView(
                    modifier = Modifier.fillMaxSize(),
                    parties = viewModel.rain()
                )
                LaunchedEffect(Unit) {
                    delay(2500L)
                    onBackClick()
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .consumedWindowInsets(innerPadding)
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                        )
                ) {
                    when {
                        uiState.isLoading -> VocabLoadingWheel(Modifier.align(Alignment.Center))
                        uiState.cards.isEmpty() -> viewModel.handleEvent(FlashcardsEvent.Empty)
                        uiState.cards.isNotEmpty() -> {
                            val swiperFlipController = rememberSwiperFlipController()

                            SideBackFrontItem(
                                modifier = Modifier.align(Alignment.TopCenter),
                                onClick = { viewModel.handleEvent(FlashcardsEvent.SideClick) },
                                isFrontSide = uiState.isFrontSide
                            )
                            Swiper(
                                items = uiState.cards,
                                swiperFlipController = swiperFlipController,
                                ttsController = ttsController,
                                onItemRemoved = { item, direction ->
                                    viewModel.handleEvent(
                                        FlashcardsEvent.RemoveCard(
                                            item = item,
                                            direction = direction
                                        )
                                    )
                                },
                                isFrontSide = uiState.isFrontSide,
                                modifier = Modifier.align(Alignment.Center)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RewindButton(
                                    addition = uiState.forgots.size,
                                    onClick = { swiperFlipController.swipeLeft() }
                                )
                                ForwardButton(
                                    addition = uiState.knows.size,
                                    onClick = { swiperFlipController.swipeRight() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuizTipsContent() {
    val spacing = LocalSpacing.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = spacing.medium,
                    vertical = spacing.normal
                )
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.quiz_tips),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            item {
                Spacer(modifier = Modifier.height(spacing.normal))
            }
            items(quizTips) { tip ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = spacing.small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(tip.tint),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = tip.icon,
                            contentDescription = stringResource(id = tip.title),
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(spacing.normal))
                    Column {
                        Text(
                            text = stringResource(id = tip.title),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(spacing.small))
                        Text(
                            text = stringResource(id = tip.description),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

private val quizTips = listOf(
    QuizTipModel(
        title = R.string.tap_the_card,
        description = R.string.view_the_reverse_side,
        icon = VocabIcons.Refresh,
        tint = Color(0xFF01579B)
    ),
    QuizTipModel(
        title = R.string.swipe_right,
        description = R.string.mark_the_card_as_known,
        icon = VocabIcons.ThumbUp,
        tint = Color(0xFF66BB6A)
    ),
    QuizTipModel(
        title = R.string.swipe_left,
        description = R.string.continue_studying_the_card,
        icon = VocabIcons.ThumbDown,
        tint = Color(0xFFEF5350)
    )
)

@Composable
private fun SideBackFrontItem(
    modifier: Modifier = Modifier,
    isFrontSide: Boolean = true,
    onClick: () -> Unit
) {
    val sidesText = if (isFrontSide) "Side 1" to "2" else "1" to "Side 2"
    val sidesColor = if (isFrontSide) {
        MaterialTheme.colorScheme.onSurface to Color.Gray
    } else Color.Gray to MaterialTheme.colorScheme.onSurface

    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = sidesColor.first)) {
                append(sidesText.first)
            }
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                append("  •  ")
            }
            withStyle(style = SpanStyle(color = sidesColor.second)) {
                append(sidesText.second)
            }
        },
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
    )
}

@Composable
private fun RewindButton(
    modifier: Modifier = Modifier,
    addition: Int,
    onClick: () -> Unit
) {
    val spacing = LocalSpacing.current

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .padding(spacing.medium)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_rewind),
            contentDescription = stringResource(id = R.string.forgot),
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(spacing.extraSmall))
        Text(
            text = "${stringResource(id = R.string.forgot)}${if (addition > 0) " $addition" else ""}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ForwardButton(
    modifier: Modifier = Modifier,
    addition: Int,
    onClick: () -> Unit
) {
    val spacing = LocalSpacing.current

    Column(
        horizontalAlignment = Alignment.End,
        modifier = modifier
            .padding(spacing.medium)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_forward),
            contentDescription = stringResource(id = R.string.know_it),
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(spacing.extraSmall))
        Text(
            text = "${stringResource(id = R.string.know_it)}${if (addition > 0) " $addition" else ""}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}