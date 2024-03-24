package dev.wazapps.timetodo.components

import android.app.AlertDialog
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import dev.wazapps.timetodo.R

@Composable
fun DisplayAlertDialog(
    title: String,
    message: String?,
    openDialog: Boolean,
    closeDialog: () -> Unit,
    onConfirmClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (openDialog) {
        AlertDialog(
            modifier = modifier,
            title = {
                Text(
                    text = title,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Bold
                )
            },
            text = message?.let {
                {
                    Text(
                        text = message,
                        fontSize = MaterialTheme.typography.labelMedium.fontSize,
                        fontWeight = FontWeight.Normal
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    onConfirmClicked()
                    closeDialog()
                }) {
                    Text(text = stringResource(R.string.alert_confirm))
                }
            },
            dismissButton = {
                Button(onClick = {
                    closeDialog()
                }) {
                    Text(text = stringResource(R.string.alert_deny))
                }
            },
            onDismissRequest = closeDialog
        )
    }
}