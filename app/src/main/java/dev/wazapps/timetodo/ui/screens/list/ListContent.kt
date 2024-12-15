package dev.wazapps.timetodo.ui.screens.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.wazapps.timetodo.components.drawable.Pin
import dev.wazapps.timetodo.components.drawable.PinSize
import dev.wazapps.timetodo.data.models.Priority
import dev.wazapps.timetodo.data.models.ToDoTask
import dev.wazapps.timetodo.ui.theme.CARD_ELEVATION
import dev.wazapps.timetodo.ui.theme.HighPriorityColor
import dev.wazapps.timetodo.ui.theme.MEDIUM_PADDING
import dev.wazapps.timetodo.ui.theme.SMALL_PADDING
import dev.wazapps.timetodo.ui.theme.VERY_LARGE_PADDING
import dev.wazapps.timetodo.utils.states.RequestState
import dev.wazapps.timetodo.utils.states.SearchAppBarState


@Composable
fun ListContent(
    modifier: Modifier,
    tasksReqState: RequestState<List<ToDoTask>>,
    searchedTasksReqState: RequestState<List<ToDoTask>>,
    searchAppBarState: SearchAppBarState,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    onSwipeToDelete: (ToDoTask) -> Unit,
    tasksSortedByLowPriority: List<ToDoTask>,
    tasksSortedByHighPriority: List<ToDoTask>,
    sortState: RequestState<Priority>
) {
    if (sortState is RequestState.Success) {
        when {
            searchAppBarState == SearchAppBarState.TRIGGERED -> {
                if (searchedTasksReqState is RequestState.Success) {
                    HandleListContent(
                        tasks = searchedTasksReqState.data,
                        navigateToTaskScreen = navigateToTaskScreen,
                        onSwipeToDelete = onSwipeToDelete,
                        modifier = modifier
                    )
                }
            }

            sortState.data == Priority.NONE -> {
                if (tasksReqState is RequestState.Success) {
                    HandleListContent(
                        tasks = tasksReqState.data,
                        navigateToTaskScreen = navigateToTaskScreen,
                        onSwipeToDelete = onSwipeToDelete,
                        modifier = modifier
                    )
                }
            }

            sortState.data == Priority.LOW -> {
                HandleListContent(
                    tasks = tasksSortedByLowPriority,
                    navigateToTaskScreen = navigateToTaskScreen,
                    onSwipeToDelete = onSwipeToDelete,
                    modifier = modifier
                )
            }

            sortState.data == Priority.HIGH -> {
                HandleListContent(
                    tasks = tasksSortedByHighPriority,
                    navigateToTaskScreen = navigateToTaskScreen,
                    onSwipeToDelete = onSwipeToDelete,
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
    onSwipeToDelete: (ToDoTask) -> Unit,
    modifier: Modifier = Modifier
) {
    if (tasks.isEmpty()) {
        EmptyContent() // display Empty State
    } else {
        TaskList(
            modifier = modifier,
            todoItems = tasks,
            onSwipeToDelete = onSwipeToDelete,
            navigateToTaskScreen = navigateToTaskScreen
        )
    }
}

@Composable
fun TaskList(
    modifier: Modifier = Modifier,
    todoItems: List<ToDoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    onSwipeToDelete: (ToDoTask) -> Unit
) {
    // TODO : use content padding to space elements, and add light grey background ?
    LazyColumn(modifier = modifier) {
        items(
            items = todoItems,
            key = { task ->
                task.id
            }) { task ->
            //var itemDismissed by remember { mutableStateOf(false) }
            // TODO : maybe use derivedStateOf instead to trigger confirm change here
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = {
                    if (it == SwipeToDismissBoxValue.StartToEnd) return@rememberSwipeToDismissBoxState false
                    if (it == SwipeToDismissBoxValue.EndToStart) {
                        onSwipeToDelete(task)
                    }
                    true
                }
            )

            val degrees by animateFloatAsState(
                targetValue =
                if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) 0f else -45f,
                label = "swipeToDismissIconRotation"
            )

            var itemAppeared by remember { mutableStateOf(false) }
            LaunchedEffect(key1 = true) {
                itemAppeared = true
            }
            AnimatedVisibility(visible = itemAppeared,
                enter = expandVertically(
                    animationSpec = tween(300),
                )
            ) {
                SwipeToDismissBox(
                    state = dismissState,
                    backgroundContent = {
                        DeletionBackground(degrees = degrees)
                    }
                ) {
                    TaskItem(toDoTask = task, navigateToTaskScreen = navigateToTaskScreen)
                }
            }
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun DeletionBackground(degrees: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HighPriorityColor, shape = RoundedCornerShape(8.dp))
            .padding(VERY_LARGE_PADDING),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            modifier = Modifier.rotate(degrees),
            imageVector = Icons.Filled.Delete,
            contentDescription = null,
            tint = Color.White
        )
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MEDIUM_PADDING),
            verticalArrangement = Arrangement.spacedBy(SMALL_PADDING)
        ) {
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

@Preview(heightDp = 80)
@Composable
private fun DeleteBackgroundPreview() {
    DeletionBackground(degrees = 25f)
}