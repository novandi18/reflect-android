package com.novandiramadhan.reflect.domain.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notification(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val type: String = NotificationType.GENERAL.value,
    val timestamp: Timestamp = Timestamp.now(),
    val isRead: Boolean = false
): Parcelable

enum class NotificationType(val value: String) {
    GENERAL("general"),
    MOOD_REMINDER("mood_reminder"),
    INSIGHT_UPDATE("insight_update"),
    JOURNAL_TIP("journal_tip"),
    REFLECTION_PROMPT("reflection_prompt"),
}