package dev.wazapps.timetodo.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import dev.wazapps.timetodo.R
import dev.wazapps.timetodo.components.drawable.Pin
import dev.wazapps.timetodo.data.models.Priority
import dev.wazapps.timetodo.ui.theme.MEDIUM_PADDING
import dev.wazapps.timetodo.ui.theme.PRIORITY_TOP_DOWN_HEIGHT
import dev.wazapps.timetodo.ui.theme.SMALL_PADDING

@Composable
fun PriorityDropDown(
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit,
    modifier: Modifier = Modifier,
    borderShape: Shape = RoundedCornerShape(SMALL_PADDING),
) {
    var expanded by remember { mutableStateOf(false) }
    val angle: Float by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "rotation"
    )
    Row(modifier = modifier
        .fillMaxWidth()
        .height(PRIORITY_TOP_DOWN_HEIGHT)
        .clickable { expanded = !expanded }
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface,
            shape = borderShape
        ),
        verticalAlignment = Alignment.CenterVertically,)
    {
        Pin(modifier = Modifier.padding(MEDIUM_PADDING), color = priority.color)
        Text(modifier = Modifier.weight(1f), text = priority.name, style = MaterialTheme.typography.headlineSmall)
        Icon(
            modifier = Modifier
                .padding(MEDIUM_PADDING)
                .alpha(.5f)
                .rotate(angle),
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = stringResource(id = R.string.acc_drop_down)
        )
        DropdownMenu(
            modifier = Modifier.fillMaxWidth(fraction = 0.94f),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = { PriorityItem(priority = Priority.LOW) },
                onClick = {
                    expanded = false
                    onPrioritySelected(Priority.LOW)
                }
            )
            DropdownMenuItem(
                text = { PriorityItem(priority = Priority.MEDIUM) },
                onClick = {
                    expanded = false
                    onPrioritySelected(Priority.MEDIUM)
                }
            )
            DropdownMenuItem(
                text = { PriorityItem(priority = Priority.HIGH) },
                onClick = {
                    expanded = false
                    onPrioritySelected(Priority.HIGH)
                }
            )
        }
    }
}

@Preview
@Composable
private fun PriorityDropDownPreview() {
    PriorityDropDown(priority = Priority.HIGH, onPrioritySelected = {})
}