package uz.javokhirdev.svocabulary.core.ui

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED
import android.speech.tts.TextToSpeech.OnInitListener
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.*

@Composable
fun rememberTtsController(): TtsController {
    val context = LocalContext.current
    return remember { TtsController(context) }
}

class TtsController constructor(context: Context) {

    private var tts: TextToSpeech? = null
    private var isLoaded = false
    private val locale = Locale.US

    init {
        try {
            val onInitListener = OnInitListener { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = locale
                    isLoaded = true
                }
            }
            tts = TextToSpeech(context, onInitListener)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun say(text: String? = null) {
        if (isLoaded) {
            val available = tts?.isLanguageAvailable(locale) ?: LANG_NOT_SUPPORTED

            if (available >= TextToSpeech.LANG_AVAILABLE) {
                tts?.language = locale
                tts?.setSpeechRate(0.85f)
                tts?.speak(
                    text,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "random string - 2d22332"
                )
            }
        }
    }

    fun shutDown() {
        tts?.shutdown()
    }
}