package dev.wazapps.timetodo.ui.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.wazapps.timetodo.R
import dev.wazapps.timetodo.ui.theme.ErrorStateIconColor
import dev.wazapps.timetodo.ui.theme.MediumGray

@Composable
fun EmptyContent() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            modifier = Modifier.size(120.dp),
            painter = painterResource(id = R.drawable.sad_face),
            contentDescription = null,
            tint = ErrorStateIconColor
        )
        Text(text = stringResource(id = R.string.empty_content_message),
            color = MediumGray,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview
@Composable
private fun EmptyContentPreview() {
    EmptyContent()
}