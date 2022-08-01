package uz.javokhirdev.svocabulary.feature.sets.presentation.navigation

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import uz.javokhirdev.svocabulary.core.navigation.VocabNavigationDestination
import uz.javokhirdev.svocabulary.feature.sets.presentation.SetsRoute

object SetsNavigation : VocabNavigationDestination {
    override val route = "sets_route"
    override val destination = "sets_destination"
}

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
fun NavGraphBuilder.setsGraph(
    navigateToSettings: () -> Unit,
    navigateToSetDetail: () -> Unit
) {
    composable(route = SetsNavigation.route) {
        SetsRoute(
            navigateToSettings = navigateToSettings,
            navigateToSetDetail = navigateToSetDetail
        )
    }
}
