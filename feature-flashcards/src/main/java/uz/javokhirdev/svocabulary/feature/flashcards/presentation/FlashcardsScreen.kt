package uz.javokhirdev.svocabulary.feature.flashcards.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabGradientBackground
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabLoadingWheel
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabTopAppBar
import uz.javokhirdev.svocabulary.core.designsystem.icon.VocabIcons
import uz.javokhirdev.svocabulary.core.designsystem.theme.LocalSpacing
import uz.javokhirdev.svocabulary.core.ui.R
import uz.javokhirdev.svocabulary.feature.flashcards.presentation.components.Swiper
import uz.javokhirdev.svocabulary.feature.flashcards.presentation.components.rememberSwiperFlipController

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun FlashcardsScreen(
    viewModel: FlashcardsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState = viewModel.uiState

    VocabGradientBackground {
        Scaffold(
            topBar = {
                VocabTopAppBar(
                    title = "",
                    navigationIcon = VocabIcons.Close,
                    onNavigationClick = onBackClick,
                    actions = {
                        IconButton(onClick = { viewModel.handleEvent(FlashcardsEvent.InfoClick) }) {
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
                LaunchedEffect(Unit) { onBackClick() }
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
                        uiState.cards.isEmpty() -> viewModel.handleEvent(FlashcardsEvent.OnEmpty)
                        uiState.cards.isNotEmpty() -> {
                            val swiperFlipController = rememberSwiperFlipController()

                            Swiper(
                                items = uiState.cards,
                                swiperFlipController = swiperFlipController,
                                onItemRemoved = { item, direction ->
                                    viewModel.handleEvent(
                                        FlashcardsEvent.RemoveCard(
                                            item = item,
                                            direction = direction
                                        )
                                    )
                                },
                                modifier = Modifier.align(Alignment.Center)
                            )

                            ArrowTextItem(
                                modifier = Modifier.align(Alignment.BottomStart),
                                text = stringResource(id = R.string.forgot),
                                addition = uiState.forgots.size,
                                icon = R.drawable.ic_arrow_rewind,
                                onClick = { swiperFlipController.swipeLeft() }
                            )
                            ArrowTextItem(
                                modifier = Modifier.align(Alignment.BottomEnd),
                                text = stringResource(id = R.string.know_it),
                                addition = uiState.knows.size,
                                icon = R.drawable.ic_arrow_forward,
                                onClick = { swiperFlipController.swipeRight() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ArrowTextItem(
    modifier: Modifier = Modifier,
    text: String,
    addition: Int,
    icon: Int,
    onClick: () -> Unit
) {
    val spacing = LocalSpacing.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(spacing.medium)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = text,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(spacing.extraSmall))
        Text(
            text = "$text${if (addition > 0) " $addition" else ""}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}