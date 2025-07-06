package com.novandiramadhan.reflect.presentation.component.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.ui.theme.Amber
import com.novandiramadhan.reflect.ui.theme.BlueGray
import com.novandiramadhan.reflect.ui.theme.LightGreen
import com.novandiramadhan.reflect.ui.theme.Teal

@Composable
fun MoodDistributionChart(
    moodDistribution: Map<String, Float>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        moodDistribution.forEach { (mood, percentage) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mood,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.width(100.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(8.dp))

                LinearProgressIndicator(
                    progress = { percentage },
                    modifier = Modifier.weight(1f),
                    color = getMoodColor(mood),
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "${(percentage * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun getMoodColor(mood: String): Color {
    return when (mood.lowercase()) {
        "sad", "very sad", "disappointed" -> BlueGray
        "uncomfortable", "okay" -> Amber
        "pretty good", "good" -> LightGreen
        "happy", "very happy", "joyful" -> Teal
        else -> MaterialTheme.colorScheme.primary
    }
}