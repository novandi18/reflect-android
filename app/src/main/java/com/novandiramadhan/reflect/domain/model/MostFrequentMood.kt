package com.novandiramadhan.reflect.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MostFrequentMood(
    val mood: String,
    val days: Int
): Parcelable
