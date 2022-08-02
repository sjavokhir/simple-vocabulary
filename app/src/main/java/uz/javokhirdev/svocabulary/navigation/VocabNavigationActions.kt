package uz.javokhirdev.svocabulary.navigation

import androidx.navigation.NavHostController
import uz.javokhirdev.svocabulary.core.data.extensions.orNotId

class VocabNavigationActions(navController: NavHostController) {

    val navigateToSetDetail = { setId: Long? ->
        navController.navigate("${Route.SET_DETAIL}/${setId.orNotId()}") {
            launchSingleTop = true
        }
    }
}