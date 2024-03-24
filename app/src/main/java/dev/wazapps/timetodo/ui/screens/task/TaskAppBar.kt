package dev.wazapps.timetodo.ui.screens.task

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import dev.wazapps.timetodo.R
import dev.wazapps.timetodo.components.DisplayAlertDialog
import dev.wazapps.timetodo.data.models.Priority
import dev.wazapps.timetodo.data.models.ToDoTask
import dev.wazapps.timetodo.ui.theme.TopAppBarContentColor
import dev.wazapps.timetodo.utils.Action

@Composable
fun TaskAppBar(
    selectedTask: ToDoTask?,
    navigateToListScreen: (Action) -> Unit
) {
    if (selectedTask == null){
        NewTaskAppBar(navigateToListScreen = navigateToListScreen)
    } else {
        ExistingTaskAppBar(
            selectedTask = selectedTask,
            navigateToListScreen = navigateToListScreen
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskAppBar(
    navigateToListScreen: (Action) -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.add_task_app_bar_title),
                color = TopAppBarContentColor
            )
        },
        navigationIcon = {
            BackAction {
                navigateToListScreen(it)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            AddAction {
                navigateToListScreen(it)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExistingTaskAppBar(
    selectedTask: ToDoTask,
    navigateToListScreen: (Action) -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = selectedTask.title,
                color = TopAppBarContentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            CloseAction {
                navigateToListScreen(it)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            ExistingTaskAppBarActions(
                navigateToListScreen = navigateToListScreen,
                selectedTask = selectedTask
            )
        }
    )

}

@Composable
fun ExistingTaskAppBarActions(navigateToListScreen: (Action) -> Unit, selectedTask: ToDoTask) {
    var openDialog by remember {
        mutableStateOf(false)
    }

    DisplayAlertDialog(
        title = stringResource(id = R.string.delete_task_alert_title),
        message = stringResource(id = R.string.delete_task_alert_subtitle, selectedTask.title),
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onConfirmClicked = { navigateToListScreen(Action.DELETE) })
    DeleteAction {
        openDialog = true
    }
    UpdateAction {
        navigateToListScreen(it)
    }
}

@Composable
fun DeleteAction(
    onDeleteClicked: () -> Unit,
) {
    IconButton(onClick = onDeleteClicked) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "Delete",
            tint = TopAppBarContentColor
        )
    }
}

@Composable
fun UpdateAction(
    onUpdateConfirmClicked: (Action) -> Unit,
) {
    IconButton(onClick = { onUpdateConfirmClicked(Action.UPDATE) }) {
        Icon(
            imageVector = Icons.Filled.Done,
            contentDescription = "Edit",
            tint = TopAppBarContentColor
        )
    }
}

@Composable
fun CloseAction(
    onCloseClicked: (Action) -> Unit,
) {
    IconButton(onClick = { onCloseClicked(Action.NO_ACTION) }) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(R.string.acc_close),
            tint = TopAppBarContentColor
        )
    }
}

@Composable
fun BackAction(
    onBackClicked: (Action) -> Unit,
) {
    IconButton(onClick = { onBackClicked(Action.NO_ACTION) }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.acc_go_back),
            tint = TopAppBarContentColor
        )
    }
}

@Composable
fun AddAction(
    onAddConfirmClicked: (Action) -> Unit,
) {
    // TODO : Maybe add a Text and an enabled state
    IconButton(onClick = { onAddConfirmClicked(Action.ADD) }) {
        Icon(
            imageVector = Icons.Filled.Done,
            contentDescription = stringResource(R.string.acc_add_task),
            tint = TopAppBarContentColor
        )
    }
}

@Preview
@Composable
private fun NewTaskAppBarPreview() {
    NewTaskAppBar {}
}

@Preview
@Composable
private fun ExistingTaskAppBarPreview() {
    ExistingTaskAppBar(
        selectedTask = ToDoTask(
            id = 0,
            title = "Here we go for a very long task just for testing stuff",
            description = "",
            priority = Priority.HIGH
        ),
        navigateToListScreen = {}
    )
}