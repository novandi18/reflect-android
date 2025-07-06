package com.novandiramadhan.reflect.presentation.component.mood_history

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
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
import com.novandiramadhan.reflect.util.state.MoodHistoryFilterState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodHistoryTopBar(
    showFilterDialog: Boolean = false,
    filterState: MoodHistoryFilterState,
    onShowFilterDialog: (Boolean) -> Unit = {},
    onFilterChange: (MoodHistoryFilterState) -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.mood_history),
                style = MaterialTheme.typography.titleMedium,
            )
        },
        actions = {
            IconButton(onClick = { onShowFilterDialog(true) }) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = stringResource(R.string.filter_entries),
                    tint = if (filterState.selectedMoods.isNotEmpty()) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
        )
    )

    if (showFilterDialog) {
        MoodHistoryFilter(
            isVisible = true,
            currentFilter = filterState,
            onApplyFilter = onFilterChange,
            onDismiss = { onShowFilterDialog(false) }
        )
    }
}

@Preview
@Composable
private fun MoodHistoryTopBarPreview() {
    ReflectTheme {
        MoodHistoryTopBar(
            filterState = MoodHistoryFilterState()
        )
    }
}
