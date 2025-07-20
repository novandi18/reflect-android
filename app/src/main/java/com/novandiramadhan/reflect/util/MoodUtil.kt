package com.novandiramadhan.reflect.util

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.MoodBad
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material.icons.filled.TagFaces
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.firebase.Timestamp
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.ui.theme.Amber
import com.novandiramadhan.reflect.ui.theme.Blue
import com.novandiramadhan.reflect.ui.theme.BlueGray
import com.novandiramadhan.reflect.ui.theme.Green
import com.novandiramadhan.reflect.ui.theme.LightGreen
import com.novandiramadhan.reflect.ui.theme.Orange
import com.novandiramadhan.reflect.ui.theme.Pink
import com.novandiramadhan.reflect.ui.theme.Purple
import com.novandiramadhan.reflect.ui.theme.Red
import com.novandiramadhan.reflect.ui.theme.Teal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getMoodIcon(mood: String, context: Context, englishOnly: Boolean = false): ImageVector {
    if (englishOnly) {
        return when (mood.lowercase()) {
            "very sad" -> Icons.Default.SentimentVeryDissatisfied
            "sad" -> Icons.Default.MoodBad
            "disappointed" -> Icons.Default.ThumbDown
            "uncomfortable" -> Icons.Default.SentimentDissatisfied
            "okay" -> Icons.Default.SentimentSatisfied
            "pretty good" -> Icons.Default.Face
            "good" -> Icons.Default.TagFaces
            "happy" -> Icons.Default.SentimentSatisfiedAlt
            "very happy" -> Icons.Default.SentimentVerySatisfied
            "joyful" -> Icons.Default.Mood
            else -> Icons.Default.Face
        }
    }

    val moodLevels = context.resources.getStringArray(R.array.mood_levels)
    val moodIndex = moodLevels.indexOf(mood)

    return when (moodIndex) {
        0 -> Icons.Default.SentimentVeryDissatisfied
        1 -> Icons.Default.MoodBad
        2 -> Icons.Default.ThumbDown
        3 -> Icons.Default.SentimentDissatisfied
        4 -> Icons.Default.SentimentSatisfied
        5 -> Icons.Default.Face
        6 -> Icons.Default.TagFaces
        7 -> Icons.Default.SentimentSatisfiedAlt
        8 -> Icons.Default.SentimentVerySatisfied
        9 -> Icons.Default.Mood
        else -> Icons.Default.Face
    }
}

fun getMoodColor(mood: String, context: Context, englishOnly: Boolean = false): Color {
    if (englishOnly) {
        return when (mood.lowercase()) {
            "very sad" -> Red
            "sad" -> Pink
            "disappointed" -> Purple
            "uncomfortable" -> Orange
            "okay" -> Amber
            "pretty good" -> BlueGray
            "good" -> Blue
            "happy" -> Teal
            "very happy" -> Green
            "joyful" -> LightGreen
            else -> Blue
        }
    }

    val moodLevels = context.resources.getStringArray(R.array.mood_levels)
    val moodIndex = moodLevels.indexOf(mood)

    return when (moodIndex) {
        0 -> Red
        1 -> Pink
        2 -> Purple
        3 -> Orange
        4 -> Amber
        5 -> BlueGray
        6 -> Blue
        7 -> Teal
        8 -> Green
        9 -> LightGreen
        else -> Blue
    }
}

fun getMoodColorByLevel(moodLevel: Int): Color {
    return when (moodLevel) {
        1 -> Red
        2 -> Pink
        3 -> Purple
        4 -> Orange
        5 -> Amber
        6 -> BlueGray
        7 -> Blue
        8 -> Teal
        9 -> Green
        10 -> LightGreen
        else -> Blue
    }
}

fun String.toTitleCase(): String {
    return split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
}

fun Date.formatToDisplayDay(): String {
    val formatter = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())
    return formatter.format(this)
}

fun Timestamp.isToday(): Boolean {
    val now = Calendar.getInstance()
    val dateCal = Calendar.getInstance().apply { time = this@isToday.toDate() }
    return now.get(Calendar.YEAR) == dateCal.get(Calendar.YEAR) &&
            now.get(Calendar.DAY_OF_YEAR) == dateCal.get(Calendar.DAY_OF_YEAR)
}

fun getCurrentWeekRange(): Pair<Date, Date> {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = System.currentTimeMillis()

    val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    val daysFromMonday = when (currentDayOfWeek) {
        Calendar.SUNDAY -> 6
        Calendar.MONDAY -> 0
        Calendar.TUESDAY -> 1
        Calendar.WEDNESDAY -> 2
        Calendar.THURSDAY -> 3
        Calendar.FRIDAY -> 4
        Calendar.SATURDAY -> 5
        else -> 0
    }

    calendar.add(Calendar.DAY_OF_YEAR, -daysFromMonday)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val startOfWeek = calendar.time

    calendar.add(Calendar.DAY_OF_YEAR, 6)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    val endOfWeek = calendar.time

    return Pair(startOfWeek, endOfWeek)
}

fun getAverageMoodFeedback(moodLevel: Double, context: Context): String {
    val feedbackArray = context.resources.getStringArray(R.array.average_mood_feedback)
    return when {
        moodLevel <= 2.0 -> feedbackArray[0]
        moodLevel <= 3.0 -> feedbackArray[1]
        moodLevel <= 4.0 -> feedbackArray[2]
        moodLevel <= 5.0 -> feedbackArray[3]
        moodLevel <= 6.0 -> feedbackArray[4]
        moodLevel <= 7.0 -> feedbackArray[5]
        moodLevel <= 8.0 -> feedbackArray[6]
        moodLevel <= 9.0 -> feedbackArray[7]
        else -> feedbackArray[8]
    }
}

fun getCurrentMonthRange(): Pair<Date, Date> {
    val calendar = Calendar.getInstance()

    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val startDate = calendar.time

    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    val endDate = calendar.time

    return Pair(startDate, endDate)
}