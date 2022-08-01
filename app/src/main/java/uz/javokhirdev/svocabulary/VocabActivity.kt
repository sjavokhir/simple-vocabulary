package uz.javokhirdev.svocabulary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import uz.javokhirdev.svocabulary.ui.VocabApp

@ExperimentalMaterial3Api
@ExperimentalLayoutApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3WindowSizeClassApi
@AndroidEntryPoint
class VocabActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent { VocabApp(/* calculateWindowSizeClass(this) */) }
    }
}
