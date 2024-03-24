package dev.wazapps.timetodo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import dev.wazapps.timetodo.navigation.destinations.listComposable
import dev.wazapps.timetodo.navigation.destinations.taskComposable
import dev.wazapps.timetodo.ui.viewmodels.TaskSharedViewModel
import dev.wazapps.timetodo.utils.Constants.LIST_SCREEN

// Eq. TimeToDoAppNavigation
@Composable
fun SetupNavigation(navController: NavHostController, taskSharedViewModel: TaskSharedViewModel) {
    val screens = remember(navController) {
            Screens(navController = navController)
    }
    NavHost(
        navController = navController,
        startDestination = LIST_SCREEN) {
            listComposable(
                navigateToTaskScreen = screens.task,
                taskSharedViewModel = taskSharedViewModel
            )
            taskComposable(
                navigateToListScreen = screens.list,
                taskSharedViewModel = taskSharedViewModel
            )
    }
}