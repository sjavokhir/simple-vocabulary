package uz.javokhirdev.svocabulary.feature.setdetail.presentation.navigation

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import uz.javokhirdev.svocabulary.core.data.Extras
import uz.javokhirdev.svocabulary.core.navigation.VocabNavigationDestination
import uz.javokhirdev.svocabulary.feature.setdetail.presentation.SetDetailScreen

object SetDetailNavigation : VocabNavigationDestination {
    override val route = "set_detail_route"
    override val destination = "set_detail_destination"
}

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
fun NavGraphBuilder.setDetailGraph(
    onBackClick: () -> Unit
) {
    composable(
        route = "${SetDetailNavigation.route}/{${Extras.SET_ID}}",
        arguments = listOf(
            navArgument(Extras.SET_ID) {
                type = NavType.LongType
            }
        )
    ) {
        SetDetailScreen(onBackClick = onBackClick)
    }
}
