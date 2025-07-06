package com.novandiramadhan.reflect.presentation.component.notifications

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTopBar(
    showActions: Boolean = true,
    onReadAllClick: () -> Unit = {},
    onDeleteAllClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.notifications),
                style = MaterialTheme.typography.titleMedium,
            )
        },
        actions = {
            if (showActions) {
                TextButton(onClick = onReadAllClick) {
                    Text(
                        text = stringResource(R.string.read_all),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                TextButton(onClick = onDeleteAllClick) {
                    Text(
                        text = stringResource(R.string.delete_all),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
        )
    )
}

@Preview
@Composable
fun NotificationTopBarPreview() {
    ReflectTheme {
        NotificationTopBar()
    }
}