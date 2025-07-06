package com.novandiramadhan.reflect.domain.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeeklySummary(
    val averageMood: Double? = null,
    val mostFrequentMood: MostFrequentMood? = null,
    val entryStreak: Int = 0,
    val topTriggers: List<String> = emptyList(),
    val dominantMood: String,
    val activeDays: Int,
    val moodDistribution: Map<String, Float>,
    val createdAt: Timestamp = Timestamp.now(),
): Parcelable
