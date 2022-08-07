package uz.javokhirdev.svocabulary.navigation

import androidx.navigation.NavHostController
import uz.javokhirdev.svocabulary.core.data.extensions.orNotId

class VocabNavigationActions(navController: NavHostController) {

    val navigateToSetDetail = { setId: Long? ->
        navController.navigate("${Route.SET_DETAIL}/${setId.orNotId()}") {
            launchSingleTop = true
        }
    }

    val navigateToCards = { setId: Long? ->
        navController.navigate("${Route.CARDS}/${setId.orNotId()}") {
            launchSingleTop = true
        }
    }

    val navigateToCardDetail = { setId: Long?, cardId: Long? ->
        navController.navigate("${Route.CARD_DETAIL}/${setId.orNotId()}/${cardId.orNotId()}") {
            launchSingleTop = true
        }
    }

    val navigateToFlashcards = { setId: Long? ->
        navController.navigate("${Route.FLASHCARDS}/${setId.orNotId()}") {
            launchSingleTop = true
        }
    }

    val navigateToSettings = {
        navController.navigate(Route.SETTINGS) {
            launchSingleTop = true
        }
    }
}