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

fun getMoodIcon(mood: String): ImageVector {
    return when (mood.toTitleCase()) {
        "Very Sad" -> Icons.Default.SentimentVeryDissatisfied
        "Sad" -> Icons.Default.MoodBad
        "Disappointed" -> Icons.Default.ThumbDown
        "Uncomfortable" -> Icons.Default.SentimentDissatisfied
        "Okay" -> Icons.Default.SentimentSatisfied
        "Pretty Good" -> Icons.Default.Face
        "Good" -> Icons.Default.TagFaces
        "Happy" -> Icons.Default.SentimentSatisfiedAlt
        "Very Happy" -> Icons.Default.SentimentVerySatisfied
        "Joyful" -> Icons.Default.Mood
        else -> Icons.Default.Face
    }
}

fun getMoodColor(mood: String): Color {
    return when (mood.toTitleCase()) {
        "Joyful" -> LightGreen
        "Very Happy" -> Green
        "Happy" -> Teal
        "Good" -> Blue
        "Pretty Good" -> BlueGray
        "Okay" -> Amber
        "Uncomfortable" -> Orange
        "Disappointed" -> Purple
        "Sad" -> Pink
        "Very Sad" -> Red
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
    // Calculate current week's Monday and Sunday
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = System.currentTimeMillis()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    // Set to Monday
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    val startOfWeek = calendar.time
    // Set to Sunday
    calendar.add(Calendar.DAY_OF_WEEK, 6)
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