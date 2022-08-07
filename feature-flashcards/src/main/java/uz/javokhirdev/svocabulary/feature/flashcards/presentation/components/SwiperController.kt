package uz.javokhirdev.svocabulary.feature.flashcards.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * To control the SwiperFlip.
 * Used to swipe card and rotate programmatically
 */
@Composable
fun rememberSwiperFlipController(): SwiperFlipController {
    return remember { SwiperFlipControllerImpl() }
}

interface SwiperFlipController {
    /**
     * Points to the top card's [FlipCardController]
     */
    var currentCardController: FlipCardController?

    fun swipeRight()
    fun swipeLeft()
    fun flip()
}

class SwiperFlipControllerImpl : SwiperFlipController {

    override var currentCardController: FlipCardController? = null

    override fun swipeRight() {
        currentCardController?.swipeRight()
    }

    override fun swipeLeft() {
        currentCardController?.swipeLeft()
    }

    override fun flip() {
        currentCardController?.setCardFlipState()
    }
}