package dev.wazapps.timetodo.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
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
    var parentSize by remember { mutableStateOf(IntSize.Zero) }
    Row(modifier = modifier
        .fillMaxWidth()
        .onGloballyPositioned { layoutCoordinates ->
            parentSize = layoutCoordinates.size
        }
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
            modifier = Modifier.width(
                with(LocalDensity.current) {
                    parentSize.width.toDp()
                }
            ),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            Priority.entries
                .minus(Priority.NONE)
                .forEach {
                    priority ->
                    DropdownMenuItem(
                        text = { PriorityItem(priority = priority) },
                        onClick = {
                            expanded = false
                            onPrioritySelected(priority)
                        }
                    )
                }
        }
    }
}

@Preview
@Composable
private fun PriorityDropDownPreview() {
    PriorityDropDown(priority = Priority.HIGH, onPrioritySelected = {})
}