package com.novandiramadhan.reflect.util

import com.google.firebase.Timestamp
import com.novandiramadhan.reflect.domain.model.Journal
import com.novandiramadhan.reflect.domain.model.Notification
import com.novandiramadhan.reflect.domain.model.NotificationType
import com.novandiramadhan.reflect.domain.model.Quote
import com.novandiramadhan.reflect.domain.model.Statistics
import com.novandiramadhan.reflect.domain.model.User
import java.util.Calendar
import java.util.Date

val dummyUser = User(
    name = "Novandi Ramadhan",
    email = "novandi@example.com",
    createdAt = Timestamp.now()
)

val dummyJournalEntries = List(7) { index ->
    Journal(
        mood = listOf("Happy", "Sad", "Uncomfortable", "Good", "Disappointed").random(),
        moodLevel = (1..10).random(),
        triggers = listOf("Work", "Family", "Weather", "Sport").shuffled().take(2),
        tags = listOf("refleksi", "syukur", "Overthinking", "harapan").shuffled().take(2),
        note = "Catatan hari ke-${index + 1}",
        createdAt = Timestamp(Calendar.getInstance().apply {
            set(Calendar.YEAR, 2025)
            set(Calendar.MONTH, Calendar.MAY)
            set(Calendar.DAY_OF_MONTH, 18)
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time)
    )
}

val dummyWeeklyStats = Statistics(
    moodSummary = mapOf(
        "Happy" to 3,
        "Sad" to 1,
        "Uncomfortable" to 2,
        "Good" to 1
    ),
    avgMoodLevel = 6.4,
    mostFrequentTrigger = "Work",
    entriesCount = 7,
    updatedAt = Timestamp(Calendar.getInstance().apply {
        set(Calendar.YEAR, 2025)
        set(Calendar.MONTH, Calendar.MAY)
        set(Calendar.DAY_OF_MONTH, 18)
        set(Calendar.HOUR_OF_DAY, 12)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time)
)

val dummyQuotes = List(7) { index ->
    val calendar = Calendar.getInstance().apply {
        time = Date()
        add(Calendar.DAY_OF_YEAR, -index)
    }
    Quote(
        quoteText = listOf(
            "Hidup adalah pelajaran berharga.",
            "Setiap hari adalah kesempatan baru.",
            "Jangan biarkan ketakutan menahanmu.",
            "Syukur adalah kunci kebahagiaan.",
            "Lepaskan yang tidak bisa kamu kendalikan."
        ).random(),
        source = listOf("Anonim", "Penulis X", "Buku Y").random()
    )
}

val dummyNotifications = listOf(
    Notification(
        id = "1",
        title = "Don't forget to log your mood",
        message = "How are you feeling today?",
        type = NotificationType.MOOD_REMINDER.value
    ),
    Notification(
        id = "2",
        title = "New Insight Available",
        message = "Your weekly mood summary is ready!",
        type = NotificationType.INSIGHT_UPDATE.value
    ),
    Notification(
        id = "3",
        title = "Tip for journaling",
        message = "Try writing about what made you smile today.",
        type = NotificationType.JOURNAL_TIP.value
    ),
    Notification(
        id = "4",
        title = "Midweek Check-in",
        message = "Take a moment to reflect on your week so far.",
        type = NotificationType.REFLECTION_PROMPT.value
    )
)
