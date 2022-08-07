package uz.javokhirdev.svocabulary.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import uz.javokhirdev.svocabulary.core.data.Extras
import uz.javokhirdev.svocabulary.feature.carddetail.presentation.CardDetailScreen
import uz.javokhirdev.svocabulary.feature.cards.presentation.CardsScreen
import uz.javokhirdev.svocabulary.feature.flashcards.presentation.FlashcardsScreen
import uz.javokhirdev.svocabulary.feature.setdetail.presentation.SetDetailScreen
import uz.javokhirdev.svocabulary.feature.sets.presentation.SetsScreen
import uz.javokhirdev.svocabulary.feature.settings.presentation.SettingsScreen

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun VocabNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navActions = remember(navController) {
        VocabNavigationActions(navController)
    }

    NavHost(
        navController = navController,
        startDestination = Route.SETS,
        modifier = modifier,
    ) {
        composable(route = Route.SETS) {
            SetsScreen(
                onSettingsClick = navActions.navigateToSettings,
                onAddSetClick = navActions.navigateToSetDetail,
                onSetClick = navActions.navigateToCards
            )
        }
        composable(
            route = "${Route.SET_DETAIL}/{${Extras.SET_ID}}",
            arguments = listOf(navArgument(Extras.SET_ID) { type = NavType.LongType })
        ) {
            SetDetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = "${Route.CARDS}/{${Extras.SET_ID}}",
            arguments = listOf(navArgument(Extras.SET_ID) { type = NavType.LongType })
        ) {
            CardsScreen(
                onBackClick = { navController.popBackStack() },
                onAddCardClick = navActions.navigateToCardDetail,
                onFlashcardsClick = navActions.navigateToFlashcards
            )
        }
        composable(
            route = "${Route.CARD_DETAIL}/{${Extras.SET_ID}}/{${Extras.CARD_ID}}",
            arguments = listOf(
                navArgument(Extras.SET_ID) { type = NavType.LongType },
                navArgument(Extras.CARD_ID) { type = NavType.LongType },
            )
        ) {
            CardDetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(route = Route.SETTINGS) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = "${Route.FLASHCARDS}/{${Extras.SET_ID}}",
            arguments = listOf(
                navArgument(Extras.SET_ID) { type = NavType.LongType },
            )
        ) {
            FlashcardsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}