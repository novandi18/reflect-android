package com.novandiramadhan.reflect.presentation.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@Composable
fun ConfirmationDialog(
    icon: ImageVector? = null,
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        icon = {
            icon?.let {
                Icon(
                    modifier = Modifier.size(36.dp),
                    imageVector = it,
                    contentDescription = title
                )
            }
        },
        onDismissRequest = onDismiss,
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(
                    text = confirmText,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = dismissText,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        iconContentColor = MaterialTheme.colorScheme.onSurface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurface
    )
}

@Preview(showBackground = true)
@Composable
fun ConfirmationDialogPreview() {
    ReflectTheme {
        ConfirmationDialog(
            icon = Icons.AutoMirrored.Rounded.Logout,
            title = "Confirm Action",
            message = "Are you sure you want to perform this action?",
            confirmText = "Confirm",
            dismissText = "Cancel",
            onConfirm = {},
            onDismiss = {}
        )
    }
}