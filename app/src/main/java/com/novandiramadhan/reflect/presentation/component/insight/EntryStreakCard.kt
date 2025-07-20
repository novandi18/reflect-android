package com.novandiramadhan.reflect.presentation.component.insight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.presentation.component.RButton
import com.novandiramadhan.reflect.ui.theme.Green
import com.novandiramadhan.reflect.ui.theme.LightGreen
import com.novandiramadhan.reflect.ui.theme.Orange
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@Composable
fun EntryStreakCard(
    modifier: Modifier = Modifier,
    totalDays: Int = 0,
    isLoading: Boolean = false,
    isError: Boolean = false,
    onRetry: () -> Unit = {}
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
                text = stringResource(R.string.entry_streak),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.entry_streak_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else if (isError) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.error_entry_streak_default),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    RButton(
                        text = stringResource(R.string.retry),
                        onClick = onRetry
                    )
                }
            } else if (totalDays > 0) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(LightGreen.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = LightGreen,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.streak_days, totalDays),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            repeat(7) { day ->
                                val isActive = day < totalDays
                                val color = if (isActive) LightGreen else LightGreen.copy(alpha = 0.3f)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(8.dp)
                                        .background(color, RoundedCornerShape(4.dp))
                                )
                            }
                        }

                        val feedbackTextRes = when {
                            totalDays >= 7 -> R.string.streak_perfect
                            totalDays >= 5 -> R.string.streak_consistent
                            totalDays >= 3 -> R.string.streak_good
                            else -> R.string.streak_getting_started
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(feedbackTextRes),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (totalDays >= 5) Green else Orange,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                EmptyState()
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.EventBusy,
                contentDescription = stringResource(R.string.no_entry_streak),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.no_entry_streak),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Preview
@Composable
private fun EntryStreakCardPreview() {
    ReflectTheme {
        EntryStreakCard(
            totalDays = 7
        )
    }
}

@Preview
@Composable
private fun EntryStreakCardPerfectPreview() {
    ReflectTheme {
        EntryStreakCard(
            totalDays = 5
        )
    }
}

@Preview
@Composable
private fun EntryStreakCardEmptyPreview() {
    ReflectTheme {
        EntryStreakCard()
    }
}

@Preview
@Composable
private fun EntryStreakCardLoadingPreview() {
    ReflectTheme {
        EntryStreakCard(
            isLoading = true
        )
    }
}

@Preview
@Composable
private fun EntryStreakCardErrorPreview() {
    ReflectTheme {
        EntryStreakCard(
            isLoading = false,
            isError = true
        )
    }
}