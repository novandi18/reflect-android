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
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, Calendar.JANUARY)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    // Find the first day of the first week
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    if (firstDayOfWeek != Calendar.SUNDAY) {
        calendar.add(Calendar.DAY_OF_MONTH, -(firstDayOfWeek - Calendar.SUNDAY))
    }

    val weeks = mutableListOf<WeekData>()
    var weekNumber = 1

    // Create end of year calendar
    val endOfYear = Calendar.getInstance()
    endOfYear.set(Calendar.YEAR, year)
    endOfYear.set(Calendar.MONTH, Calendar.DECEMBER)
    endOfYear.set(Calendar.DAY_OF_MONTH, 31)
    endOfYear.set(Calendar.HOUR_OF_DAY, 23)
    endOfYear.set(Calendar.MINUTE, 59)
    endOfYear.set(Calendar.SECOND, 59)

    // Generate all weeks for the entire year
    while (!calendar.after(endOfYear)) {
        val startDate = calendar.time

        calendar.add(Calendar.DAY_OF_MONTH, 6)
        val endDate = calendar.time

        weeks.add(WeekData(weekNumber, startDate, endDate))

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        weekNumber++
    }

    return weeks
}

fun isSameDay(date1: Date, date2: Date): Boolean {
    val cal1 = Calendar.getInstance().apply { time = date1 }
    val cal2 = Calendar.getInstance().apply { time = date2 }
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}