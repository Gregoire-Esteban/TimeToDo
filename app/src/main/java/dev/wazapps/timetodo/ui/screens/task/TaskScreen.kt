package dev.wazapps.timetodo.ui.screens.task

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import dev.wazapps.timetodo.R
import dev.wazapps.timetodo.data.models.ToDoTask
import dev.wazapps.timetodo.ui.viewmodels.TaskSharedViewModel
import dev.wazapps.timetodo.utils.Action

@Composable
fun TaskScreen(
    navigateToListScreen: (Action) -> Unit,
    sharedViewModel: TaskSharedViewModel,
    selectedTask: ToDoTask?
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        topBar = {
            TaskAppBar(
                selectedTask = selectedTask,
                navigateToListScreen = { action ->
                    if (action == Action.NO_ACTION){
                        navigateToListScreen(action)
                    } else {
                        if (sharedViewModel.validateFields()) {
                            keyboardController?.hide()
                            navigateToListScreen(action)
                        } else {
                            displayToast(context)
                        }
                    }
                },
                validRulesChecked = sharedViewModel.fieldsValid
            )
        }
    ) { paddingValues ->
        val title by sharedViewModel.title
        val description by sharedViewModel.description
        val priority by sharedViewModel.priority
        TaskContent(
            modifier = Modifier.padding(paddingValues),
            title = title,
            onTitleChanged = {
                sharedViewModel.updateTitle(it)
            },
            description = description,
            onDescriptionChanged = {
                sharedViewModel.description.value = it
            },
            priority = priority,
            onPrioritySelected = {
                sharedViewModel.priority.value = it
            }
        )

        BackHandler {
            navigateToListScreen(Action.NO_ACTION)
        }
    }
}

fun displayToast(context: Context) {
    Toast.makeText(
        context,
        context.getString(R.string.save_task_error_message),
        Toast.LENGTH_LONG
    ).show()
}
