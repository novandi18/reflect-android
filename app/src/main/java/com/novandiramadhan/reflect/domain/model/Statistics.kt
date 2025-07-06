package com.novandiramadhan.reflect.domain.model

import com.google.firebase.Timestamp

data class Statistics(
    val moodSummary: Map<String, Int> = emptyMap(),
    val avgMoodLevel: Double = 0.0,
    val mostFrequentTrigger: String = "",
    val entriesCount: Int = 0,
    val updatedAt: Timestamp? = null
)