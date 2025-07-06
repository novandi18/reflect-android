package com.novandiramadhan.reflect.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.presentation.component.insight.AverageMoodLevelCard
import com.novandiramadhan.reflect.presentation.component.insight.EntryStreakCard
import com.novandiramadhan.reflect.presentation.component.insight.MoodTrendChart
import com.novandiramadhan.reflect.presentation.component.insight.MostFrequentMoodCard
import com.novandiramadhan.reflect.presentation.component.insight.TopTriggersCard
import com.novandiramadhan.reflect.presentation.viewmodel.InsightViewModel
import com.novandiramadhan.reflect.ui.theme.ReflectTheme
import androidx.compose.runtime.getValue
import com.novandiramadhan.reflect.presentation.state.AverageMoodUiState
import com.novandiramadhan.reflect.presentation.state.EntryStreakUiState
import com.novandiramadhan.reflect.presentation.state.MoodTrendUiState
import com.novandiramadhan.reflect.presentation.state.MostFrequentMoodUiState
import com.novandiramadhan.reflect.presentation.state.TopTriggersUiState

@Composable
fun InsightScreen(
    viewModel: InsightViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()
    val moodTrendChartState by viewModel.moodTrendChartState.collectAsState()
    val averageMood by viewModel.averageMood.collectAsState()
    val moodTrend by viewModel.moodTrend.collectAsState()
    val mostFrequentMood by viewModel.mostFrequentMood.collectAsState()
    val entryStreakState by viewModel.entryStreak.collectAsState()
    val topTriggers by viewModel.topTriggers.collectAsState()

    val backgroundColor = MaterialTheme.colorScheme.background
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawRect(backgroundColor)
                    drawContent()
                }
        )

        Image(
            painter = painterResource(id = R.drawable.background_doodle),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                backgroundColor,
                                Color.Transparent,
                                Color.Transparent,
                            ),
                            startY = 0f,
                            endY = size.height * 2f
                        )
                    )
                }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AverageMoodLevelCard(
                moodLevel = if (averageMood is AverageMoodUiState.Success) {
                    (averageMood as AverageMoodUiState.Success).average
                } else null,
                isLoading = averageMood is AverageMoodUiState.Loading,
                isError = averageMood is AverageMoodUiState.Error,
                onRetry = {
                    viewModel.getAverageMoodInWeek(user.id)
                }
            )

            MostFrequentMoodCard(
                data = if (mostFrequentMood is MostFrequentMoodUiState.Success) {
                    (mostFrequentMood as MostFrequentMoodUiState.Success).data
                } else null,
                isLoading = mostFrequentMood is MostFrequentMoodUiState.Loading,
                isError = mostFrequentMood is MostFrequentMoodUiState.Error,
                onRetry = {
                    viewModel.getMostFrequentMoodInWeek(user.id)
                }
            )

            MoodTrendChart(
                moodData = if (moodTrend is MoodTrendUiState.Success) {
                    (moodTrend as MoodTrendUiState.Success).data
                } else emptyList(),
                isLoading = moodTrend is MoodTrendUiState.Loading,
                isError = moodTrend is MoodTrendUiState.Error,
                chartState = moodTrendChartState,
                onUpdateSelectedWeek = { weekIndex ->
                    viewModel.updateSelectedWeek(weekIndex)
                },
                onUpdateSelectedYear = { year ->
                    viewModel.updateSelectedYear(year)
                },
                onToggleWeekDropdown = { isExpanded ->
                    viewModel.toggleWeekDropdown(isExpanded)
                },
                onToggleYearDropdown = { isExpanded ->
                    viewModel.toggleYearDropdown(isExpanded)
                },
                onRetry = {
                    viewModel.getMoodTrendInWeek(user.id)
                }
            )

            EntryStreakCard(
                totalDays = if (entryStreakState is EntryStreakUiState.Success) {
                    (entryStreakState as EntryStreakUiState.Success).totalDays ?: 0
                } else 0,
                isLoading = entryStreakState is EntryStreakUiState.Loading,
                isError = entryStreakState is EntryStreakUiState.Error,
                onRetry = {
                    viewModel.getEntryStreakDays(user.id)
                }
            )

            TopTriggersCard(
                triggers = if (topTriggers is TopTriggersUiState.Success) {
                    (topTriggers as TopTriggersUiState.Success).triggers
                } else emptyList(),
                isLoading = topTriggers is TopTriggersUiState.Loading,
                isError = topTriggers is TopTriggersUiState.Error,
                onRetry = {
                    viewModel.getTopTriggers(user.id)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InsightScreenPreview() {
    ReflectTheme {
        InsightScreen()
    }
}