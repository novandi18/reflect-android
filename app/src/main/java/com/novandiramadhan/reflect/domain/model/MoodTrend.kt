package com.novandiramadhan.reflect.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class MoodTrendData(
    val date: Date,
    val moodLevel: Int,
    val mood: String
): Parcelable

data class WeekData(
    val weekNumber: Int,
    val startDate: Date,
    val endDate: Date
)
