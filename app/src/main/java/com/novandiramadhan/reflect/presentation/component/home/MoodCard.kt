package com.novandiramadhan.reflect.presentation.component.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.ui.theme.ReflectTheme
import com.novandiramadhan.reflect.util.getMoodIcon
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MoodCard(
    mood: String?,
    moodLevel: Int?,
    timestamp: Timestamp?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val hasData = mood != null && moodLevel != null && timestamp != null

    val formattedTime = if (hasData) {
        val date = timestamp?.toDate()
        val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        formatter.format(date ?: return)
    } else "Unknown time"

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
                text = stringResource(R.string.mood_card_title_last),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (hasData) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (mood != null) {
                        Icon(
                            imageVector = getMoodIcon(mood, context),
                            contentDescription = null,
                            tint = getMoodColor(mood),
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    if (mood != null && moodLevel != null) {
                        Column {
                            Text(
                                text = mood,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = stringResource(R.string.mood_level, moodLevel),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.last_recorded, formattedTime),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📝",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = stringResource(R.string.no_last_mood_description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoodCardPreview() {
    ReflectTheme {
        MoodCard(
            mood = "Happy",
            moodLevel = 7,
            timestamp = Timestamp.now()
        )
    }
}
