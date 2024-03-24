package dev.wazapps.timetodo.ui.screens.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.wazapps.timetodo.R
import dev.wazapps.timetodo.components.PriorityDropDown
import dev.wazapps.timetodo.data.models.Priority
import dev.wazapps.timetodo.ui.theme.LARGE_PADDING
import dev.wazapps.timetodo.ui.theme.SMALL_PADDING

@Composable
fun TaskContent(
    title: String,
    onTitleChanged: (String) -> Unit,
    description: String,
    onDescriptionChanged: (String) -> Unit,
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(LARGE_PADDING),
        verticalArrangement = Arrangement.spacedBy(SMALL_PADDING)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = onTitleChanged,
            label = {
                Text(
                    text = stringResource(R.string.title)
                )
            },
            singleLine = true
        )
        PriorityDropDown(
            modifier = Modifier.fillMaxWidth(),
            priority = priority,
            onPrioritySelected = onPrioritySelected
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = description,
            onValueChange = onDescriptionChanged,
            label = {
                Text(
                    text = stringResource(R.string.description)
                )
            }
        )
    }
}

@Preview
@Composable
private fun TaskContentPreview() {
    TaskContent(
        title = "Bonjour",
        onTitleChanged = {},
        description = "This is a new task to tackle with the best possible tact and tactic",
        onDescriptionChanged = {},
        priority = Priority.HIGH,
        onPrioritySelected = {}
    )
}