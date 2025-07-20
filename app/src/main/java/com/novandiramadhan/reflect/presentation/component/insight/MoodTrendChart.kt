package com.novandiramadhan.reflect.presentation.component.insight

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.domain.model.MoodTrendData
import com.novandiramadhan.reflect.presentation.component.RButton
import com.novandiramadhan.reflect.presentation.state.component.MoodTrendChartState
import com.novandiramadhan.reflect.ui.theme.ReflectTheme
import com.novandiramadhan.reflect.util.isSameDay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.novandiramadhan.reflect.util.getMoodColorByLevel
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorCount
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties

@Composable
fun MoodTrendChart(
    modifier: Modifier = Modifier,
    moodData: List<MoodTrendData> = emptyList(),
    isLoading: Boolean = false,
    isError: Boolean = false,
    chartState: MoodTrendChartState = MoodTrendChartState(),
    onUpdateSelectedWeek: (Int) -> Unit = {},
    onUpdateSelectedYear: (Int) -> Unit = {},
    onToggleWeekDropdown: (Boolean) -> Unit = {},
    onToggleYearDropdown: (Boolean) -> Unit = {},
    onRetry: () -> Unit = {}
) {
    val context = LocalContext.current
    val currentCalendar = Calendar.getInstance()
    val currentYear = currentCalendar.get(Calendar.YEAR)
    val availableYears = listOf(currentYear, currentYear - 1, currentYear - 2)

    val selectedWeekData = if (chartState.weeksInYear.isNotEmpty() &&
        chartState.selectedWeekIndex < chartState.weeksInYear.size) {
        chartState.weeksInYear[chartState.selectedWeekIndex]
    } else null

    val filteredData = if (selectedWeekData != null) {
        moodData.filter { mood ->
            mood.date.time >= selectedWeekData.startDate.time &&
                    mood.date.time <= selectedWeekData.endDate.time
        }.sortedBy { it.date }
    } else emptyList()

    val weekDays = if (selectedWeekData != null) {
        val calendar = Calendar.getInstance().apply {
            time = selectedWeekData.startDate
        }
        val days = mutableListOf<MoodTrendData>()
        repeat(7) {
            val dayDate = calendar.time
            val existingMood = filteredData.find { isSameDay(it.date, dayDate) }

            days.add(
                existingMood ?: MoodTrendData(
                    date = dayDate,
                    moodLevel = 0,
                    mood = ""
                )
            )

            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        days
    } else emptyList()

    val weekDayFormat = SimpleDateFormat("EEE", Locale.getDefault())
    val barsData = remember(weekDays) {
        weekDays.map { dayData ->
            Bars(
                label = weekDayFormat.format(dayData.date),
                values = listOf(
                    Bars.Data(
                        label = dayData.mood,
                        value = dayData.moodLevel.toDouble(),
                        color = if (dayData.moodLevel > 0) {
                            val moodColor = getMoodColorByLevel(dayData.moodLevel)
                            Brush.verticalGradient(listOf(moodColor, moodColor))
                        } else {
                            Brush.verticalGradient(listOf(Color.Gray, Color.DarkGray))
                        }
                    )
                )
            )
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
        ),
        shape = CardDefaults.shape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = stringResource(R.string.mood_trends),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (isError) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
                ) {
                    Text(
                        text = stringResource(R.string.error_mood_trend),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    RButton(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        text = stringResource(R.string.retry),
                        onClick = onRetry,
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                .clickable {
                                    onToggleWeekDropdown(!chartState.showWeekDropdown)
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val weekText = if (selectedWeekData != null) {
                                if (
                                    chartState.selectedYear == currentYear &&
                                    chartState.selectedWeekIndex == chartState.currentWeekIndex
                                ) {
                                    stringResource(R.string.this_week)
                                } else {
                                    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                                    stringResource(
                                        R.string.filter_week_choose,
                                        selectedWeekData.weekNumber,
                                        dateFormat.format(selectedWeekData.startDate)
                                    )
                                }
                            } else stringResource(R.string.select_week)

                            Text(
                                text = weekText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = stringResource(R.string.select_week),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        DropdownMenu(
                            expanded = chartState.showWeekDropdown,
                            onDismissRequest = {
                                onToggleWeekDropdown(!chartState.showWeekDropdown)
                            },
                            containerColor = MaterialTheme.colorScheme.surface,
                            shadowElevation = 0.dp
                        ) {
                            if (
                                chartState.selectedYear == currentYear &&
                                chartState.currentWeekIndex >= 0 &&
                                chartState.currentWeekIndex < chartState.weeksInYear.size
                            ) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.this_week)) },
                                    onClick = {
                                        onUpdateSelectedWeek(chartState.currentWeekIndex)
                                        onToggleWeekDropdown(false)
                                    },
                                    colors = MenuItemColors(
                                        textColor = MaterialTheme.colorScheme.onSurface,
                                        leadingIconColor = MaterialTheme.colorScheme.onSurface,
                                        trailingIconColor = MaterialTheme.colorScheme.onSurface,
                                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(.5f),
                                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(.5f),
                                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(.5f),
                                    )
                                )
                            }

                            val visibleWeeks = when {
                                chartState.selectedYear > currentYear -> emptyList()
                                chartState.selectedYear == currentYear -> chartState.weeksInYear.filterIndexed { i, _ -> i <= chartState.currentWeekIndex }
                                else -> chartState.weeksInYear
                            }

                            for (i in visibleWeeks.indices.reversed()) {
                                val weekIndex = chartState.weeksInYear.indexOf(visibleWeeks[i])
                                val week = visibleWeeks[i]

                                if (chartState.selectedYear == currentYear && weekIndex == chartState.currentWeekIndex) {
                                    continue
                                }

                                val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                                val weekLabel = stringResource(
                                    R.string.filter_week_choose,
                                    week.weekNumber,
                                    dateFormat.format(week.startDate)
                                )

                                DropdownMenuItem(
                                    text = { Text(weekLabel) },
                                    onClick = {
                                        if (weekIndex != chartState.selectedWeekIndex) {
                                            onUpdateSelectedWeek(weekIndex)
                                            onToggleWeekDropdown(false)
                                        } else onToggleWeekDropdown(false)
                                    },
                                    colors = MenuItemColors(
                                        textColor = MaterialTheme.colorScheme.onSurface,
                                        leadingIconColor = MaterialTheme.colorScheme.onSurface,
                                        trailingIconColor = MaterialTheme.colorScheme.onSurface,
                                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(.5f),
                                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(.5f),
                                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(.5f),
                                    )
                                )
                            }
                        }
                    }

                    Box {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                .clickable {
                                    onToggleYearDropdown(!chartState.showYearDropdown)
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = chartState.selectedYear.toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = stringResource(R.string.select_year),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        DropdownMenu(
                            expanded = chartState.showYearDropdown,
                            onDismissRequest = {
                                onToggleYearDropdown(!chartState.showYearDropdown)
                            },
                            containerColor = MaterialTheme.colorScheme.surface,
                            shadowElevation = 0.dp,
                            offset = DpOffset(
                                x = 0.dp,
                                y = 8.dp
                            )
                        ) {
                            availableYears.forEach { year ->
                                DropdownMenuItem(
                                    text = { Text(year.toString()) },
                                    onClick = {
                                        if (chartState.currentWeekIndex != chartState.selectedWeekIndex) {
                                            onUpdateSelectedYear(chartState.currentWeekIndex)
                                            onToggleYearDropdown(false)
                                        } else onToggleYearDropdown(false)
                                    },
                                    colors = MenuItemColors(
                                        textColor = MaterialTheme.colorScheme.onSurface,
                                        leadingIconColor = MaterialTheme.colorScheme.onSurface,
                                        trailingIconColor = MaterialTheme.colorScheme.onSurface,
                                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(.5f),
                                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(.5f),
                                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(.5f),
                                    )
                                )
                            }
                        }
                    }
                }

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    if (weekDays.any { it.moodLevel > 0 }) {
                        ColumnChart(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .padding(bottom = 16.dp),
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            barProperties = BarProperties(
                                cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
                                spacing = 0.dp,
                                thickness = 35.dp
                            ),
                            data = barsData,
                            minValue = 0.0,
                            maxValue = 10.0,
                            labelProperties = LabelProperties(
                                rotation = LabelProperties.Rotation(
                                    degree = 0f
                                ),
                                enabled = true,
                                textStyle = TextStyle(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 12.sp
                                )
                            ),
                            gridProperties = GridProperties(
                                enabled = true,
                                xAxisProperties = GridProperties.AxisProperties(
                                    lineCount = 11
                                ),
                                yAxisProperties = GridProperties.AxisProperties(
                                    lineCount = 8
                                )
                            ),
                            labelHelperProperties = LabelHelperProperties(
                                enabled = false
                            ),
                            indicatorProperties = HorizontalIndicatorProperties(
                                count = IndicatorCount.CountBased(11),
                                padding = 8.dp,
                                contentBuilder = { value -> value.toInt().toString() },
                                textStyle = TextStyle(
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_mood_trend_data),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MoodTrendChartPreview() {
    val dummyMoodData = listOf(
        MoodTrendData(
            date = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }.time,
            moodLevel = 9,
            mood = "Happy"
        ),
        MoodTrendData(
            date = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -6) }.time,
            moodLevel = 7,
            mood = "Dissatisfied"
        )
    )

    ReflectTheme(
        darkTheme = false
    ) {
        MoodTrendChart(moodData = dummyMoodData)
    }
}

@Preview
@Composable
private fun MoodTrendChartLoadingPreview() {
    ReflectTheme(
        darkTheme = false
    ) {
        MoodTrendChart(
            moodData = emptyList(),
            isLoading = true
        )
    }
}

@Preview
@Composable
private fun MoodTrendChartErrorPreview() {
    ReflectTheme(
        darkTheme = false
    ) {
        MoodTrendChart(
            moodData = emptyList(),
            isLoading = false,
            isError = true
        )
    }
}