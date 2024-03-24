package dev.wazapps.timetodo.data.models

import androidx.compose.ui.graphics.Color
import dev.wazapps.timetodo.ui.theme.HighPriorityColor
import dev.wazapps.timetodo.ui.theme.LowPriorityColor
import dev.wazapps.timetodo.ui.theme.MediumPriorityColor
import dev.wazapps.timetodo.ui.theme.NonePriorityColor

enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)
}