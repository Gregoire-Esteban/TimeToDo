package dev.wazapps.timetodo.ui.screens.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import dev.wazapps.timetodo.ui.theme.SMALL_PADDING
import dev.wazapps.timetodo.ui.viewmodels.TaskSharedViewModel
import dev.wazapps.timetodo.utils.Action
import dev.wazapps.timetodo.utils.states.SearchAppBarState
import kotlinx.coroutines.launch

@Composable
fun ListScreen(navigateToTaskScreen: (taskId: Int) -> Unit, sharedViewModel: TaskSharedViewModel) {
    LaunchedEffect(key1 = true) {
        sharedViewModel.getAllTasks()
        sharedViewModel.readSortingState()
    }
    val allTasksReqState by sharedViewModel.allTasks.collectAsState()
    val searchedTasksReqState by sharedViewModel.searchedTasks.collectAsState()
    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState
    val tasksSortedByLowPriority by sharedViewModel.tasksSortedByLowPriority.collectAsState()
    val tasksSortedByHighPriority by sharedViewModel.tasksSortedByHighPriority.collectAsState()
    val sortState by sharedViewModel.sortState.collectAsState()

    val latestAction = sharedViewModel.action
    val snackbarHostState = remember { SnackbarHostState() }
    DisplaySnackbar(
        snackbarHostState = snackbarHostState,
        executeAction = { sharedViewModel.executeAction(latestAction) },
        taskTitle = sharedViewModel.title.value,
        onUndoClicked = { sharedViewModel.updateAction(it) },
        action = latestAction
    )
    Scaffold(
        topBar = {
            ListAppBar(
                sharedViewModel,
                searchAppBarState,
                searchTextState,
                sortState
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            ListFab(navigateToTaskScreen = navigateToTaskScreen)
        },
    ) {
        paddingValues ->
        //region eventually to refacto in a new fun
        ListContent(
            modifier = Modifier
                .padding(paddingValues)
                .padding(SMALL_PADDING),
            tasksReqState = allTasksReqState,
            searchedTasksReqState = searchedTasksReqState,
            searchAppBarState = searchAppBarState,
            navigateToTaskScreen = navigateToTaskScreen,
            tasksSortedByLowPriority = tasksSortedByLowPriority,
            tasksSortedByHighPriority = tasksSortedByHighPriority,
            sortState = sortState,
            onSwipeToDelete = {
                sharedViewModel.updateAction(Action.DELETE)
                sharedViewModel.updateTaskFields(selectedTask = it)
            }
        )
        //endregion
    }
}



@Composable
fun ListFab(
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    FloatingActionButton(onClick = {
        navigateToTaskScreen(-1)
    }) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = null)
    }
}

@Composable
fun DisplaySnackbar(
    snackbarHostState: SnackbarHostState,
    executeAction: () -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action
) {

    executeAction()
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action) {
        if (action == Action.DELETE) {
            scope.launch {
                val snackbarResult = snackbarHostState.showSnackbar(
                    message = "Task $taskTitle deleted",
                    actionLabel = "UNDO",
                    withDismissAction = true
                )
                undoDeleteTask(action, snackbarResult, onUndoClicked)
            }
        }
        if (action != Action.NO_ACTION) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = getSnackbarMessage(action, taskTitle),
                    withDismissAction = true
                )
            }
        }
    }
}

// TODO : Maybe set a template system with Action enum
private fun getSnackbarMessage(action: Action, taskTitle: String?) =
    when(action) {
        Action.DELETE_ALL -> "All tasks removed"
        else -> "Action ${action.name} executed successfully for $taskTitle"
    }

private fun undoDeleteTask(
    action: Action,
    snackbarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
) {
    if(snackbarResult == SnackbarResult.ActionPerformed && action == Action.DELETE) {
        onUndoClicked(Action.UNDO)
    }
}

/*
@Preview
@Composable
private fun ListScreenPreview() {
    ListScreen {}
}
 */