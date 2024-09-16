package dev.wazapps.timetodo.components

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import dev.wazapps.timetodo.components.drawable.Pin
import dev.wazapps.timetodo.components.drawable.PinSize
import dev.wazapps.timetodo.data.models.Priority
import dev.wazapps.timetodo.ui.theme.MEDIUM_PADDING

@Composable
fun PriorityItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    priority: Priority,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = spacedBy(MEDIUM_PADDING)
    ) {
        Pin(color = priority.color, pinSize = PinSize.Small)
        Text(
            text = priority.name,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Preview
@Composable
private fun PriorityItemPreview() {
    PriorityItem(priority = Priority.MEDIUM)
}