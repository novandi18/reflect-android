package com.novandiramadhan.reflect.presentation.component.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.domain.model.WeeklyStats
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@Composable
fun WeeklyStatsCard(
    modifier: Modifier = Modifier,
    weeklyStats: WeeklyStats? = null,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = CardDefaults.shape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.weekly_stats_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (weeklyStats != null) {
                Text(
                    text = stringResource(R.string.dominant_mood, weeklyStats.dominantMood),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.active_days, weeklyStats.activeDays, 7),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.mood_distribution),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                MoodDistributionChart(moodDistribution = weeklyStats.moodDistribution)
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = stringResource(R.string.no_weekly_data_title),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = stringResource(R.string.no_weekly_data_description),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 3
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeeklyStatsCardPreview() {
    val sampleData = WeeklyStats(
        dominantMood = "Happy",
        activeDays = 5,
        moodDistribution = mapOf(
            "Happy" to 0.45f,
            "Good" to 0.25f,
            "Okay" to 0.2f,
            "Sad" to 0.1f
        )
    )

    ReflectTheme {
        WeeklyStatsCard(
            weeklyStats = sampleData,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeeklyStatsCardEmptyPreview() {
    ReflectTheme {
        WeeklyStatsCard(
            weeklyStats = null,
            modifier = Modifier.padding(16.dp)
        )
    }
}