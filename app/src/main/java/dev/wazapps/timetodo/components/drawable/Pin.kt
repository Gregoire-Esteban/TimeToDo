package dev.wazapps.timetodo.components.drawable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import dev.wazapps.timetodo.ui.theme.ICON_SIZE
import dev.wazapps.timetodo.ui.theme.TINY_ICON_SIZE

@Composable
fun Pin(
    color: Color,
    modifier: Modifier = Modifier,
    pinSize: PinSize = PinSize.Medium
) {
    Canvas(
        modifier = modifier
            .size(pinSize.size)
    ) {
        drawCircle(color = color)
    }
}

sealed class PinSize(val size: Dp) {
    data object Small: PinSize(TINY_ICON_SIZE)
    data object Medium: PinSize(ICON_SIZE)
}

