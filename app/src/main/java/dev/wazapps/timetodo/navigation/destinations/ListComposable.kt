package dev.wazapps.timetodo.navigation.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.wazapps.timetodo.ui.screens.list.ListScreen
import dev.wazapps.timetodo.ui.viewmodels.TaskSharedViewModel
import dev.wazapps.timetodo.utils.Action
import dev.wazapps.timetodo.utils.Constants.LIST_ARGUMENT_KEY
import dev.wazapps.timetodo.utils.Constants.LIST_SCREEN


fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (Int) -> Unit,
    taskSharedViewModel: TaskSharedViewModel
) {
    composable(
        route = LIST_SCREEN,
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY) {
            type = NavType.StringType
        })
    ) { navBackStackEntry ->
        val rawAction = navBackStackEntry.arguments?.getString(LIST_ARGUMENT_KEY)
        LaunchedEffect(key1 = rawAction) {
            taskSharedViewModel.action.value = Action.fromString(rawAction)
        }
        ListScreen(
            navigateToTaskScreen = navigateToTaskScreen,
            sharedViewModel = taskSharedViewModel
        )
    }
}