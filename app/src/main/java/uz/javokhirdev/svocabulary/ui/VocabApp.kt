package uz.javokhirdev.svocabulary.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.compose.rememberNavController
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabBackground
import uz.javokhirdev.svocabulary.core.designsystem.theme.VocabTheme
import uz.javokhirdev.svocabulary.navigation.VocabNavHost

@ExperimentalAnimationApi
@ExperimentalLayoutApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
fun VocabApp() {
    VocabTheme {
        val navController = rememberNavController()

        VocabBackground {
            Scaffold(
                modifier = Modifier.semantics {
                    testTagsAsResourceId = true
                },
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) { padding ->
                VocabNavHost(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                        .padding(padding)
                )
            }
        }
    }
}