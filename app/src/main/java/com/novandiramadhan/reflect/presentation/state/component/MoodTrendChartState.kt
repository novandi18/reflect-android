package com.novandiramadhan.reflect.presentation.state.component

import com.novandiramadhan.reflect.domain.model.WeekData
import java.util.Calendar

data class MoodTrendChartState(
    val selectedWeekIndex: Int = 0,
    val selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val showWeekDropdown: Boolean = false,
    val showYearDropdown: Boolean = false,
    val currentWeekIndex: Int = 0,
    val weeksInYear: List<WeekData> = emptyList()
)
