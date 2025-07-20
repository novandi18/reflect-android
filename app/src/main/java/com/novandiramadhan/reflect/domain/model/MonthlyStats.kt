package com.novandiramadhan.reflect.domain.model

data class MonthlyStats(
    val dominantMood: String,
    val activeDays: Int,
    val moodDistribution: Map<String, Float>
)
