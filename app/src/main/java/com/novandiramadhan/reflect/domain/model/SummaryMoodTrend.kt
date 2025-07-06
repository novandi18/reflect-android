package com.novandiramadhan.reflect.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SummaryMoodTrend(
    val week: Int,
    val averageMoodLevel: Double,
    val averageMood: String
): Parcelable