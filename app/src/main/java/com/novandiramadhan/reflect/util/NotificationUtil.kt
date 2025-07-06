package com.novandiramadhan.reflect.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.novandiramadhan.reflect.domain.model.NotificationType
import com.novandiramadhan.reflect.ui.theme.Amber
import com.novandiramadhan.reflect.ui.theme.Blue
import com.novandiramadhan.reflect.ui.theme.Orange
import com.novandiramadhan.reflect.ui.theme.Purple
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun getNotificationTypeInfo(type: String): Pair<ImageVector, Color> {
    return when (type) {
        NotificationType.GENERAL.value -> Icons.Default.Info to MaterialTheme.colorScheme.primary
        NotificationType.MOOD_REMINDER.value -> Icons.Default.Notifications to Orange
        NotificationType.INSIGHT_UPDATE.value -> Icons.AutoMirrored.Filled.ShowChart to Blue
        NotificationType.JOURNAL_TIP.value -> Icons.Default.Lightbulb to Amber
        NotificationType.REFLECTION_PROMPT.value -> Icons.Default.Psychology to Purple
        else -> Icons.Default.Info to MaterialTheme.colorScheme.primary
    }
}

fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60 * 1000 -> "Just now"
        diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)} minutes ago"
        diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)} hours ago"
        diff < 48 * 60 * 60 * 1000 -> "Yesterday"
        else -> SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(timestamp))
    }
}

