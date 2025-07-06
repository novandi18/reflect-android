package com.novandiramadhan.reflect.presentation.component.report_bug

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
fun ReportBugTopBar(
    back: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.report_bug_title),
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
fun ReportBugTopBarPreview() {
    ReflectTheme {
        ReportBugTopBar()
    }
}