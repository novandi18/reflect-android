package com.novandiramadhan.reflect.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeeklyStats(
    val dominantMood: String,
    val activeDays: Int,
    val moodDistribution: Map<String, Float>
): Parcelable