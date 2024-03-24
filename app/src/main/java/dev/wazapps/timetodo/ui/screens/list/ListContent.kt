package dev.wazapps.timetodo.ui.screens.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.wazapps.timetodo.components.drawable.Pin
import dev.wazapps.timetodo.components.drawable.PinSize
import dev.wazapps.timetodo.data.models.Priority
import dev.wazapps.timetodo.data.models.ToDoTask
import dev.wazapps.timetodo.ui.theme.CARD_ELEVATION
import dev.wazapps.timetodo.ui.theme.MEDIUM_PADDING
import dev.wazapps.timetodo.ui.theme.SMALL_PADDING
import dev.wazapps.timetodo.utils.states.RequestState
import dev.wazapps.timetodo.utils.states.SearchAppBarState


@Composable
fun ListContent(
    modifier: Modifier,
    tasksReqState: RequestState<List<ToDoTask>>,
    searchedTasksReqState: RequestState<List<ToDoTask>>,
    searchAppBarState: SearchAppBarState,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    tasksSortedByLowPriority: List<ToDoTask>,
    tasksSortedByHighPriority: List<ToDoTask>,
    sortState: RequestState<Priority>
) {
    if (sortState is RequestState.Success){
        when {
            searchAppBarState == SearchAppBarState.TRIGGERED -> {
                if (searchedTasksReqState is RequestState.Success) {
                    HandleListContent(
                        tasks = searchedTasksReqState.data,
                        navigateToTaskScreen = navigateToTaskScreen,
                        modifier = modifier
                    )
                }
            }
            sortState.data == Priority.NONE -> {
                if (tasksReqState is RequestState.Success) {
                    HandleListContent(
                        tasks = tasksReqState.data,
                        navigateToTaskScreen = navigateToTaskScreen,
                        modifier = modifier
                    )
                }
            }
            sortState.data == Priority.LOW -> {
                HandleListContent(
                    tasks = tasksSortedByLowPriority,
                    navigateToTaskScreen = navigateToTaskScreen,
                    modifier = modifier
                )
            }
            sortState.data == Priority.HIGH -> {
                HandleListContent(
                    tasks = tasksSortedByHighPriority,
                    navigateToTaskScreen = navigateToTaskScreen,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun HandleListContent(
    tasks: List<ToDoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (tasks.isEmpty()) {
        EmptyContent()
    } else {
        TaskList(
            modifier = modifier,
            todoItems = tasks,
            navigateToTaskScreen = navigateToTaskScreen
        )
    }
}

@Composable
fun TaskList(
    modifier: Modifier = Modifier,
    todoItems: List<ToDoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(
            items = todoItems,
            key = { task ->
                task.id
            }) {task ->
            TaskItem(toDoTask = task,
                navigateToTaskScreen = navigateToTaskScreen
            )
        }
    }
}

@Composable
fun TaskItem(
    toDoTask: ToDoTask,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        //color = TaskItemBackgroundColor
        shape = RoundedCornerShape(8.dp),
        shadowElevation = CARD_ELEVATION,
        onClick = {
            navigateToTaskScreen(toDoTask.id)
        }
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(MEDIUM_PADDING),
            verticalArrangement = Arrangement.spacedBy(SMALL_PADDING)) {
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = toDoTask.title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Pin(color = toDoTask.priority.color, pinSize = PinSize.Small)
            }
            Text(text = toDoTask.description)
        }
    }
}

@Preview
@Composable
private fun TaskItemPreview() {
    TaskItem(
        toDoTask = ToDoTask(
            title = "Awsome Title",
            description = "How about doing this, because It would add great value to our happiness ?",
            priority = Priority.HIGH
        )
    ) {
        
    }
}