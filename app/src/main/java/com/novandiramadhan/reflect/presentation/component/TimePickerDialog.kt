package com.novandiramadhan.reflect.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    state: TimePickerState,
    onDismiss: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(0.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onDismiss
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.close),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text(
                        text = stringResource(R.string.choose_time),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                }

                TextButton(
                    modifier = Modifier.padding(end = 4.dp),
                    onClick = {
                        onTimeSelected(state.hour, state.minute)
                        onDismiss()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.select),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TimePicker(
                    state = state,
                    colors = TimePickerDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        clockDialColor = MaterialTheme.colorScheme.surface,
                        clockDialSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                        selectorColor = MaterialTheme.colorScheme.primary,
                        timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                        timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                        timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surface,
                        timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
                        periodSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                        periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
                        periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                        periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TimePickerDialogPreview() {
    ReflectTheme {
        val state = rememberTimePickerState(
            initialHour = 10,
            initialMinute = 30
        )
        TimePickerDialog(
            state = state,
            onDismiss = {},
            onTimeSelected = { hour, minute -> }
        )
    }
}
