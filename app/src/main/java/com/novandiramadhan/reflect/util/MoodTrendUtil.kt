package com.novandiramadhan.reflect.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.novandiramadhan.reflect.domain.model.WeekData
import java.util.Calendar
import java.util.Date

fun DrawScope.drawGridLines(maxHeight: Float) {
    val lineStops = listOf(0f, 0.5f, 1f)
    lineStops.forEach { stop ->
        drawLine(
            color = Color.Gray.copy(alpha = 0.3f),
            start = Offset(40f, size.height - 20f - (stop * maxHeight)),
            end = Offset(size.width, size.height - 20f - (stop * maxHeight)),
            strokeWidth = 1f
        )
    }
}

fun generateWeeksForYear(year: Int): List<WeekData> {
    val weeks = mutableListOf<WeekData>()
    val calendar = Calendar.getInstance()

    // Start from January 1st of the year
    calendar.set(year, Calendar.JANUARY, 1, 0, 0, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    // Find first Monday of the year (or last Monday of previous year)
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

    var weekNumber = 1
    while (calendar.get(Calendar.YEAR) <= year) {
        val startDate = calendar.time

        // Move to Sunday (end of week)
        calendar.add(Calendar.DAY_OF_YEAR, 6)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endDate = calendar.time

        weeks.add(WeekData(
            weekNumber = weekNumber,
            startDate = startDate,
            endDate = endDate
        ))

        // Move to next Monday
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        weekNumber++

        // Stop if we've moved past the target year
        if (calendar.get(Calendar.YEAR) > year) break
    }

    return weeks
}

fun isSameDay(date1: Date, date2: Date): Boolean {
    val cal1 = Calendar.getInstance().apply { time = date1 }
    val cal2 = Calendar.getInstance().apply { time = date2 }
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}