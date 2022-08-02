package uz.javokhirdev.svocabulary.navigation

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
import uz.javokhirdev.svocabulary.feature.setdetail.presentation.SetDetailScreen
import uz.javokhirdev.svocabulary.feature.sets.presentation.SetsScreen

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
                onSettingsClick = {},
                onAddSetClick = navActions.navigateToSetDetail
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
    }
}