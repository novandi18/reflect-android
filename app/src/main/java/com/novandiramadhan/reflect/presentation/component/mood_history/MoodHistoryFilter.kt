package com.novandiramadhan.reflect.presentation.component.mood_history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.ui.theme.Green
import com.novandiramadhan.reflect.ui.theme.ReflectTheme
import com.novandiramadhan.reflect.util.state.MoodHistoryFilterState

@Composable
fun MoodHistoryFilter(
    isVisible: Boolean,
    currentFilter: MoodHistoryFilterState,
    onApplyFilter: (MoodHistoryFilterState) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isVisible) return

    var selectedMoods by remember(currentFilter.selectedMoods) {
        mutableStateOf(currentFilter.selectedMoods)
    }
    var sortByDateDescending by remember(currentFilter.sortByDateDescending) {
        mutableStateOf(currentFilter.sortByDateDescending)
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(R.string.filter_entries),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        tint = Green
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = stringResource(R.string.filter_by_mood),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                val moodLevels = stringArrayResource(R.array.mood_levels).toList()

                moodLevels.forEach { mood ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedMoods.contains(mood),
                            onCheckedChange = { checked ->
                                selectedMoods = if (checked) {
                                    selectedMoods + mood
                                } else {
                                    selectedMoods - mood
                                }
                            }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = mood,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Sort,
                        contentDescription = null,
                        tint = Green
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = stringResource(R.string.sort_by),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column(modifier = Modifier.selectableGroup()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = sortByDateDescending,
                                onClick = { sortByDateDescending = true },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = sortByDateDescending,
                            onClick = null
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = stringResource(R.string.newest_first),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = !sortByDateDescending,
                                onClick = { sortByDateDescending = false },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = !sortByDateDescending,
                            onClick = null
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = stringResource(R.string.oldest_first),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            selectedMoods = emptyList()
                            sortByDateDescending = true
                        }
                    ) {
                        Text(text = stringResource(R.string.clear))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(onClick = onDismiss) {
                        Text(text = stringResource(R.string.cancel))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            onApplyFilter(
                                MoodHistoryFilterState(
                                    selectedMoods = selectedMoods,
                                    sortByDateDescending = sortByDateDescending
                                )
                            )
                            onDismiss()
                        }
                    ) {
                        Text(text = stringResource(R.string.apply))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoodHistoryFilterPreview() {
    ReflectTheme {
        MoodHistoryFilter(
            isVisible = true,
            currentFilter = MoodHistoryFilterState(),
            onApplyFilter = {},
            onDismiss = {}
        )
    }
}
