package com.novandiramadhan.reflect.presentation.component.summary

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlySummaryTopBar(
    back: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.monthly_summary_channel_name),
                style = MaterialTheme.typography.titleMedium,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        navigationIcon = {
            IconButton(
                onClick = back,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null,
                )
            }
        }
    )
}

@Preview
@Composable
fun MonthlySummaryTopBarPreview() {
    ReflectTheme {
        MonthlySummaryTopBar()
    }
}