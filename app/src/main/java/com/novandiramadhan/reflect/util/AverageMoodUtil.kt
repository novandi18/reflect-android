package com.novandiramadhan.reflect.util

import androidx.compose.ui.graphics.Color
import com.novandiramadhan.reflect.ui.theme.Amber
import com.novandiramadhan.reflect.ui.theme.Blue
import com.novandiramadhan.reflect.ui.theme.BlueGray
import com.novandiramadhan.reflect.ui.theme.Green
import com.novandiramadhan.reflect.ui.theme.LightGreen
import com.novandiramadhan.reflect.ui.theme.Orange
import com.novandiramadhan.reflect.ui.theme.Purple
import com.novandiramadhan.reflect.ui.theme.Red
import com.novandiramadhan.reflect.ui.theme.Teal

fun getMoodLevelColor(level: Int): Color {
    return when (level) {
        9 -> LightGreen
        8 -> Green
        7 -> Teal
        6 -> Blue
        5 -> BlueGray
        4 -> Amber
        3 -> Orange
        2 -> Purple
        1 -> Red
        else -> Red
    }
}

fun getMoodAverageLevelColor(level: Double): Color {
    return when {
        level >= 9.0 -> LightGreen
        level >= 8.0 -> Green
        level >= 7.0 -> Teal
        level >= 6.0 -> Blue
        level >= 5.0 -> BlueGray
        level >= 4.0 -> Amber
        level >= 3.0 -> Orange
        level >= 2.0 -> Purple
        level >= 1.0 -> Red
        else -> Red
    }
}

fun getMoodLevelDescription(level: Double): String {
    return when {
        level >= 9.0 -> "Excellent"
        level >= 8.0 -> "Very Good"
        level >= 7.0 -> "Good"
        level >= 6.0 -> "Above Average"
        level >= 5.0 -> "Average"
        level >= 4.0 -> "Below Average"
        level >= 3.0 -> "Fair"
        level >= 2.0 -> "Poor"
        level >= 1.0 -> "Very Poor"
        else -> "Invalid"
    }
}