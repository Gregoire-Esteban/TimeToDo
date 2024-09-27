package dev.wazapps.timetodo.navigation.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.wazapps.timetodo.navigation.Screens
import dev.wazapps.timetodo.ui.screens.list.ListScreen
import dev.wazapps.timetodo.ui.viewmodels.TaskSharedViewModel
import dev.wazapps.timetodo.utils.Action


fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (Int) -> Unit,
    taskSharedViewModel: TaskSharedViewModel
) {
    composable<Screens.List> { navBackStackEntry ->
        val actionToPerform = navBackStackEntry.toRoute<Screens.List>().action
        var lastAction by rememberSaveable { mutableStateOf(Action.NO_ACTION) }

        LaunchedEffect(key1 = lastAction) {
            if (actionToPerform != lastAction) {
                lastAction = actionToPerform
                taskSharedViewModel.updateAction(actionToPerform)
            }
        }

        val updatedAction = taskSharedViewModel.action
        ListScreen(
            navigateToTaskScreen = navigateToTaskScreen,
            sharedViewModel = taskSharedViewModel,
            actionToPerform = updatedAction
        )
    }
}