package dev.wazapps.timetodo.navigation.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.wazapps.timetodo.ui.screens.task.TaskScreen
import dev.wazapps.timetodo.ui.viewmodels.TaskSharedViewModel
import dev.wazapps.timetodo.utils.Action
import dev.wazapps.timetodo.utils.Constants.TASK_ARGUMENT_KEY
import dev.wazapps.timetodo.utils.Constants.TASK_SCREEN


fun NavGraphBuilder.taskComposable(
    navigateToListScreen: (Action) -> Unit,
    taskSharedViewModel: TaskSharedViewModel
) {
    composable(
        route = TASK_SCREEN,
        arguments = listOf(navArgument(TASK_ARGUMENT_KEY) {
            type = NavType.IntType
        })
    ) { navBackStackEntry ->
        val taskId = navBackStackEntry.arguments?.getInt(TASK_ARGUMENT_KEY) ?: -1
        LaunchedEffect(key1 = taskId) {
            taskSharedViewModel.getSelectedTask(taskId)
        }
        val selectedTask by taskSharedViewModel.selectedTask.collectAsState()
        LaunchedEffect(key1 = selectedTask) {
            if (selectedTask != null || taskId == -1) taskSharedViewModel.updateTaskFields(selectedTask)
        }
        TaskScreen(
            selectedTask = selectedTask,
            sharedViewModel = taskSharedViewModel,
            navigateToListScreen = navigateToListScreen
        )
    }
}