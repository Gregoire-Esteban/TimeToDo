package dev.wazapps.timetodo.ui.screens.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.wazapps.timetodo.R
import dev.wazapps.timetodo.components.DisplayAlertDialog
import dev.wazapps.timetodo.components.PriorityItem
import dev.wazapps.timetodo.data.models.Priority
import dev.wazapps.timetodo.ui.theme.MEDIUM_PADDING
import dev.wazapps.timetodo.ui.theme.TopAppBarContentColor
import dev.wazapps.timetodo.ui.viewmodels.TaskSharedViewModel
import dev.wazapps.timetodo.utils.Action
import dev.wazapps.timetodo.utils.states.RequestState
import dev.wazapps.timetodo.utils.states.SearchAppBarState

@Composable
fun ListAppBar(
    sharedViewModel: TaskSharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String,
    sortState: RequestState<Priority>
) {
    when (searchAppBarState) {
        SearchAppBarState.CLOSED -> {
            DefaultListAppBar(
                onSearchedClicked = {
                    sharedViewModel.searchAppBarState.value = SearchAppBarState.OPENED
                },
                onSortClicked = {
                    sharedViewModel.persistSortingState(it)
                },
                onDeleteAllClicked = {
                    sharedViewModel.executeAction(Action.DELETE_ALL)
                },
                selectedPriority = sortState.let {
                    (it as? RequestState.Success)?.data ?: Priority.NONE
                }
            )
        }
        else -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = { sharedViewModel.searchTextState.value = it },
                onCloseClicked = {
                    sharedViewModel.searchAppBarState.value = SearchAppBarState.CLOSED
                    sharedViewModel.searchTextState.value = ""
                },
                onSearchedClicked = { query -> sharedViewModel.searchDatabase(query) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultListAppBar(
    onSearchedClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllClicked: () -> Unit,
    selectedPriority: Priority
) {
    TopAppBar(
        title = {
            Text(text = "Tasks", color = TopAppBarContentColor)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            ListAppBarActions(
                onSearchedClicked = onSearchedClicked,
                onSortClicked = onSortClicked,
                onDeleteAllConfirmed = onDeleteAllClicked,
                selectedPriority = selectedPriority
            )
        }
    )
}

@Composable
fun ListAppBarActions(
    onSearchedClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit,
    selectedPriority: Priority
) {
    var openDialog by remember {
        mutableStateOf(false)
    }

    DisplayAlertDialog(
        title = stringResource(id = R.string.delete_all_tasks_alert_title),
        message = null,
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onConfirmClicked = onDeleteAllConfirmed)

    SearchAction(onSearchedClicked)
    SortAction(onSortClicked, selectedPriority)
    DeleteAllAction { openDialog = true }
}

@Composable
fun SearchAction(
    onSearchedClicked: () -> Unit
) {
    IconButton(onClick = onSearchedClicked) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(R.string.acc_search_action),
            tint = TopAppBarContentColor
        )
    }
}

@Composable
fun SortAction(
    onSortClicked: (Priority) -> Unit,
    selectedPriority: Priority = Priority.NONE
) {
    var expandState by remember { mutableStateOf(false) }
    IconButton(onClick = { expandState = true }) {
        Icon(
            painter = painterResource(id = R.drawable.filter_list),
            contentDescription = stringResource(R.string.acc_sort_action),
            tint = TopAppBarContentColor
        )
        DropdownMenu(
            expanded = expandState,
            onDismissRequest = { expandState = false }
        ) {
            DropdownMenuItem(
                text = {
                    PriorityItem(
                        priority = Priority.HIGH,
                        isSelected = selectedPriority == Priority.HIGH
                    )
                },
                onClick = {
                    expandState = false
                    onSortClicked(Priority.HIGH)
                }
            )
            DropdownMenuItem(
                text = {
                    PriorityItem(
                        priority = Priority.LOW,
                        isSelected = selectedPriority == Priority.LOW
                    )
                },
                onClick = {
                    expandState = false
                    onSortClicked(Priority.LOW)
                }
            )
            DropdownMenuItem(
                text = {
                    PriorityItem(
                        priority = Priority.NONE,
                        isSelected = selectedPriority == Priority.NONE
                    )
                },
                onClick = {
                    expandState = false
                    onSortClicked(Priority.NONE)
                }
            )
        }
    }
}

@Composable
fun DeleteAllAction(
    onDeleteClicked: () -> Unit
) {
    var expandState by remember { mutableStateOf(false) }
    IconButton(onClick = { expandState = true }) {
        Icon(
            painter = painterResource(id = R.drawable.vertical_menu),
            contentDescription = stringResource(R.string.acc_delete_all_action),
            tint = TopAppBarContentColor
        )
        DropdownMenu(
            expanded = expandState,
            onDismissRequest = { expandState = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        modifier = Modifier.padding(MEDIUM_PADDING),
                        text = stringResource(R.string.delete_all_action),
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                onClick = {
                    expandState = false
                    onDeleteClicked()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchedClicked: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TopAppBarDefaults.TopAppBarExpandedHeight),
        shadowElevation = 4.dp,
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            shape = RectangleShape,
            onValueChange = { onTextChange(it) },
            placeholder = { Text(text = stringResource(R.string.search_placeholder), color = Color.White) },
            textStyle = TextStyle(
                color = TopAppBarContentColor,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(R.string.acc_search_icon),
                        tint = TopAppBarContentColor
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    if (text.isNotEmpty()){
                        onTextChange("")
                    } else {
                        onCloseClicked()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.acc_close),
                        tint = TopAppBarContentColor
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchedClicked(text)
                }
            ),
            colors = TextFieldDefaults.colors(
                cursorColor = TopAppBarContentColor,
                focusedContainerColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}


@Preview
@Composable
private fun SearchAppBarPreview() {
    SearchAppBar(
        text = "Hola",
        onTextChange = {},
        onCloseClicked = {},
        onSearchedClicked = {}
    )
}


@Preview
@Composable
private fun DefaultListAppBarPreview() {
    DefaultListAppBar(
        onSearchedClicked = {},
        onSortClicked = {},
        onDeleteAllClicked = {},
        selectedPriority = Priority.LOW
    )
}