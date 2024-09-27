package dev.wazapps.timetodo.navigation.destinations

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.wazapps.timetodo.navigation.Screens
import dev.wazapps.timetodo.ui.screens.task.TaskScreen
import dev.wazapps.timetodo.ui.viewmodels.TaskSharedViewModel
import dev.wazapps.timetodo.utils.Action


fun NavGraphBuilder.taskComposable(
    navigateToListScreen: (Action) -> Unit,
    taskSharedViewModel: TaskSharedViewModel
) {
    composable<Screens.Task>(
        enterTransition = { slideInHorizontally(
            animationSpec = tween(400),
            initialOffsetX = { fullWidth ->  -fullWidth }
        ) }
    ) { navBackStackEntry ->
        val taskId = navBackStackEntry.toRoute<Screens.Task>().id
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