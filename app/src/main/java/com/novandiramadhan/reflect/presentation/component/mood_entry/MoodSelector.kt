package com.novandiramadhan.reflect.presentation.component.mood_entry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.domain.model.MoodOption
import com.novandiramadhan.reflect.presentation.component.home.MoodItem
import com.novandiramadhan.reflect.ui.theme.ReflectTheme
import com.novandiramadhan.reflect.util.getMoodColor
import com.novandiramadhan.reflect.util.getMoodIcon

@Composable
fun MoodSelector(
    selectedMood: String,
    onMoodSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val moodLabels = context.resources.getStringArray(R.array.mood_levels)

    val moods = (1..10).map { level ->
        MoodOption(
            level = level,
            icon = getMoodIcon(moodLabels[level - 1], context),
            label = moodLabels[level - 1],
            color = getMoodColor(moodLabels[level - 1], context)
        )
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.choose_today_mood),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(370.dp)
        ) {
            items(moods) { mood ->
                MoodItem(
                    mood = mood,
                    isSelected = selectedMood == mood.label,
                    onSelect = { onMoodSelected(mood.label) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoodSelectorPreview() {
    var selectedMood by remember { mutableStateOf("Okay") }

    ReflectTheme(
        darkTheme = true
    ) {
        MoodSelector(
            selectedMood = selectedMood,
            onMoodSelected = { mood ->
                selectedMood = mood
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}