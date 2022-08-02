package uz.javokhirdev.svocabulary.navigation

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import uz.javokhirdev.svocabulary.core.data.extensions.orNotId
import uz.javokhirdev.svocabulary.feature.setdetail.presentation.navigation.SetDetailNavigation
import uz.javokhirdev.svocabulary.feature.setdetail.presentation.navigation.setDetailGraph
import uz.javokhirdev.svocabulary.feature.sets.presentation.navigation.SetsNavigation
import uz.javokhirdev.svocabulary.feature.sets.presentation.navigation.setsGraph

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun VocabNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = SetsNavigation.route,
        modifier = modifier,
    ) {
        setsGraph(
            navigateToSettings = {},
            navigateToSetDetail = { navController.navigate("${SetDetailNavigation.route}/${it.orNotId()}") },
        )
        setDetailGraph(
            onBackClick = { navController.popBackStack() }
        )
    }
}
