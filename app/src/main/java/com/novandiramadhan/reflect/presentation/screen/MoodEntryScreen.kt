package com.novandiramadhan.reflect.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.presentation.component.RTextField
import com.novandiramadhan.reflect.presentation.component.mood_entry.MoodSelector
import com.novandiramadhan.reflect.presentation.state.JournalEntryUiState
import com.novandiramadhan.reflect.presentation.viewmodel.MoodEntryViewModel
import com.novandiramadhan.reflect.ui.theme.Green
import com.novandiramadhan.reflect.ui.theme.Red
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@Composable
fun MoodEntryScreen(
    viewModel: MoodEntryViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val user by viewModel.user.collectAsState()
    val form by viewModel.form.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val allTriggers by viewModel.triggers.collectAsState()
    val searchTrigger by viewModel.searchTriggerName.collectAsState()
    var tagInput by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        when (uiState) {
            is JournalEntryUiState.Success -> {
                Toast.makeText(
                    context,
                    (uiState as JournalEntryUiState.Success).message,
                    Toast.LENGTH_SHORT
                ).show()
                onNavigateBack()
                viewModel.resetState()
            }
            is JournalEntryUiState.Error -> {
                Toast.makeText(
                    context,
                    (uiState as JournalEntryUiState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MoodSelector(
            selectedMood = form.mood,
            onMoodSelected = { mood ->
                viewModel.updateMood(mood)
            }
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.mood_triggers_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.mood_triggers_description),
                    style = MaterialTheme.typography.bodyMedium
                )

                RTextField(
                    value = searchTrigger,
                    onValueChange = {
                        viewModel.updateSearchTriggerName(it)
                        viewModel.filterTriggers()
                    },
                    placeholder = stringResource(R.string.mood_triggers_search),
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    borderColor = MaterialTheme.colorScheme.onBackground
                )

                if (form.triggers.size < 3) {
                    Text(
                        text = stringResource(R.string.mood_triggers_error),
                        style = MaterialTheme.typography.bodySmall,
                        color = Red
                    )
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    form.triggers.forEach { trigger ->
                        AssistChip(
                            onClick = {
                                viewModel.updateTriggers(form.triggers - trigger)
                            },
                            label = { Text(trigger) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                labelColor = MaterialTheme.colorScheme.onPrimary,
                            ),
                            border = null
                        )
                    }
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    allTriggers
                        .filter { it !in form.triggers }
                        .take(10)
                        .forEach { trigger ->
                            AssistChip(
                                onClick = {
                                    viewModel.updateTriggers(form.triggers + trigger)
                                },
                                label = { Text(trigger) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = MaterialTheme.colorScheme.background,
                                    labelColor = MaterialTheme.colorScheme.onBackground,
                                )
                            )
                        }
                }
            }
        }

        // Mood Intensity Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.mood_intensity_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.mood_intensity_description),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = form.moodLevel.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )

                Slider(
                    value = form.moodLevel.toFloat(),
                    onValueChange = { viewModel.updateMoodLevel(it.toInt()) },
                    valueRange = 1f..10f,
                    steps = 9,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        activeTrackColor = Green,
                        inactiveTrackColor = MaterialTheme.colorScheme.background,
                        thumbColor = Green,
                        disabledActiveTrackColor = Green.copy(alpha = 0.5f),
                        disabledInactiveTrackColor = MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                        disabledThumbColor = Green.copy(alpha = 0.5f)
                    )
                )
            }
        }

        // Tags Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.mood_tags_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.mood_tags_description),
                    style = MaterialTheme.typography.bodyMedium
                )

                RTextField(
                    value = tagInput,
                    onValueChange = { tagInput = it },
                    placeholder = stringResource(R.string.mood_tags_input),
                    imeAction = ImeAction.Done,
                    onDone = {
                        if (tagInput.isNotBlank()) {
                            val tag = tagInput.trim().replace(Regex("^#+"), "").lowercase().replace(" ", "_")
                            if (tag.isNotBlank() && tag !in form.tags) {
                                viewModel.updateTags(form.tags + tag)
                            }
                            tagInput = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = MaterialTheme.colorScheme.onBackground
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    form.tags.forEach { tag ->
                        AssistChip(
                            onClick = {
                                viewModel.updateTags(form.tags - tag)
                            },
                            label = { Text("#$tag") },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                labelColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            border = null
                        )
                    }
                }
            }
        }

        // Journal Entry Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.mood_journal_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.mood_journal_description),
                    style = MaterialTheme.typography.bodyMedium
                )

                RTextField(
                    value = form.note,
                    onValueChange = { viewModel.updateNote(it) },
                    placeholder = stringResource(R.string.mood_journal_placeholder),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 10,
                    minLines = 5,
                    imeAction = ImeAction.Default,
                    borderColor = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Button(
            onClick = {
                if (user.id.isNotEmpty()) viewModel.addJournal(user.id)
            },
            enabled = viewModel.validateForm() && uiState !is JournalEntryUiState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            if (uiState is JournalEntryUiState.Loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(stringResource(R.string.save_entry))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoodEntryScreenPreview() {
    ReflectTheme {
        MoodEntryScreen()
    }
}

