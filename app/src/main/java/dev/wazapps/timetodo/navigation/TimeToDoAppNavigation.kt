package dev.wazapps.timetodo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.wazapps.timetodo.navigation.destinations.listComposable
import dev.wazapps.timetodo.navigation.destinations.taskComposable
import dev.wazapps.timetodo.ui.viewmodels.TaskSharedViewModel
import dev.wazapps.timetodo.utils.Action

// Eq. TimeToDoAppNavigation
@Composable
fun SetupNavigation(navController: NavHostController, taskSharedViewModel: TaskSharedViewModel) {

    NavHost(
        navController = navController,
        startDestination = Screens.List(Action.NO_ACTION)) {
            listComposable(
                navigateToTaskScreen = { taskId ->
                    navController.navigate(Screens.Task(id = taskId))
                },
                taskSharedViewModel = taskSharedViewModel
            )
            taskComposable(
                navigateToListScreen = { action ->
                    navController.navigate(Screens.List(action = action)) {
                        popUpTo(Screens.Task(-1)) { inclusive = true }
                    }
                },
                taskSharedViewModel = taskSharedViewModel
            )
    }
}