package uz.javokhirdev.svocabulary.feature.flashcards.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabResponsiveText
import uz.javokhirdev.svocabulary.core.designsystem.icon.VocabIcons
import uz.javokhirdev.svocabulary.core.designsystem.theme.LocalSpacing
import uz.javokhirdev.svocabulary.core.model.CardModel
import uz.javokhirdev.svocabulary.core.ui.TtsController

@Composable
fun Swiper(
    modifier: Modifier = Modifier,
    items: List<CardModel>,
    onItemRemoved: (CardModel, SwipedOutDirection) -> Unit,
    swiperFlipController: SwiperFlipController,
    ttsController: TtsController,
    isFrontSide: Boolean = true,
    stackCount: Int = 2,
    paddingBetweenCards: Float = 40f
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Box(modifier = modifier) {
        val list = items.take(stackCount).reversed()

        list.forEachIndexed { index, item ->
            key(item) {
                val flipCardController = rememberFlipCardController()

                if (index == list.lastIndex) {
                    swiperFlipController.currentCardController = flipCardController
                }

                if (!flipCardController.isCardOut()) {
                    val paddingTop by animateFloatAsState(targetValue = (index * paddingBetweenCards))

                    Surface(
                        modifier = modifier
                            .size(screenWidth - 48.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                flipCardController.setCardFlipState()
                            }
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragEnd = {
                                        flipCardController.onDragEnd()
                                    },
                                    onDragCancel = {
                                        flipCardController.onDragCancel()
                                    },
                                    onDrag = { change, dragAmount ->
                                        if (change.positionChange() != Offset.Zero) change.consume()
                                        flipCardController.onDrag(dragAmount)
                                    }
                                )
                            }
                            .graphicsLayer {
                                translationX = flipCardController.cardX
                                translationY = flipCardController.cardY + paddingTop
                                rotationZ = flipCardController.rotation
                                rotationY = flipCardController.cardFlipRotation
                                cameraDistance = 12 * density
                            },
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        shadowElevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 6.dp
                        ).shadowElevation(
                            enabled = true,
                            interactionSource = null
                        ).value,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(screenWidth - 48.dp)
                                .zIndex(1f - flipCardController.cardBackAlpha)
                                .graphicsLayer {
                                    alpha = flipCardController.cardFrontAlpha
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            AnimatedVisibility(
                                visible = isFrontSide,
                                enter = fadeIn(animationSpec = tween(200)),
                                exit = fadeOut(animationSpec = tween(200))
                            ) {
                                FlashcardItem(
                                    text = item.term,
                                    ttsController = ttsController
                                )
                            }
                            AnimatedVisibility(
                                visible = !isFrontSide,
                                enter = fadeIn(animationSpec = tween(200)),
                                exit = fadeOut(animationSpec = tween(200))
                            ) {
                                FlashcardItem(
                                    text = item.definition,
                                    ttsController = ttsController
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .size(screenWidth - 48.dp)
                                .zIndex(1f - flipCardController.cardFrontAlpha)
                                .graphicsLayer {
                                    alpha = flipCardController.cardBackAlpha
                                    rotationY = -180f
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            AnimatedVisibility(
                                visible = isFrontSide,
                                enter = fadeIn(animationSpec = tween(200)),
                                exit = fadeOut(animationSpec = tween(200))
                            ) {
                                FlashcardItem(
                                    text = item.definition,
                                    ttsController = ttsController
                                )
                            }
                            AnimatedVisibility(
                                visible = !isFrontSide,
                                enter = fadeIn(animationSpec = tween(200)),
                                exit = fadeOut(animationSpec = tween(200))
                            ) {
                                FlashcardItem(
                                    text = item.term,
                                    ttsController = ttsController
                                )
                            }
                        }
                    }
                } else {
                    flipCardController.swipedOutDirection?.let { outDirection ->
                        onItemRemoved(item, outDirection)
                    }
                }
            }
        }
    }
}

@Composable
fun FlashcardItem(
    text: String? = null,
    ttsController: TtsController
) {
    val spacing = LocalSpacing.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.extraNormal)
    ) {
        VocabResponsiveText(
            text = text.orEmpty(),
            textStyle = MaterialTheme.typography.titleLarge.copy(
                fontSize = 56.sp
            ),
            modifier = Modifier.align(Alignment.Center)
        )
        IconButton(
            onClick = { ttsController.say(text) },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Icon(
                imageVector = VocabIcons.VolumeUp,
                contentDescription = text,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}