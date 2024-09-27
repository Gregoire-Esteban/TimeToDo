package dev.wazapps.timetodo.navigation

import dev.wazapps.timetodo.utils.Action
import kotlinx.serialization.Serializable

//class Screens(navController: NavHostController) {
//    val list: (Action) -> Unit = { action ->
//        navController.navigate("list/${action.name}") {
//            popUpTo(LIST_SCREEN) { inclusive = true }
//        }
//    }
//
//    val task: (Int) -> Unit = { taskId ->
//        navController.navigate("task/$taskId")
//    }
//}

@Serializable
sealed class Screens {
    @Serializable
    data class List(val action: Action = Action.NO_ACTION) : Screens()
    @Serializable
    data class Task(val id: Int) : Screens()
}