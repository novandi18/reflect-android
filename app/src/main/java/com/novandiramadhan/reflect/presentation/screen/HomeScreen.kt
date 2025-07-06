package com.novandiramadhan.reflect.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import com.novandiramadhan.reflect.presentation.component.home.DailyQuoteCard
import com.novandiramadhan.reflect.presentation.component.home.MoodCard
import com.novandiramadhan.reflect.presentation.component.home.ReminderCard
import com.novandiramadhan.reflect.presentation.component.home.WeeklyStatsCard
import com.novandiramadhan.reflect.presentation.navigation.Destinations
import com.novandiramadhan.reflect.presentation.viewmodel.HomeViewModel
import com.novandiramadhan.reflect.ui.theme.ReflectTheme
import com.novandiramadhan.reflect.util.isToday
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.novandiramadhan.reflect.presentation.component.home.DailyQuoteSkeleton
import com.novandiramadhan.reflect.presentation.component.home.HomeMoodQuoteSkeleton
import com.novandiramadhan.reflect.presentation.component.home.WeeklyStatsCardSkeleton
import com.novandiramadhan.reflect.presentation.state.DailyQuoteUiState
import com.novandiramadhan.reflect.presentation.state.LastMoodUiState
import com.novandiramadhan.reflect.presentation.state.WeeklyStatsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigate: (Destinations) -> Unit = {}
) {
    val user by viewModel.user.collectAsState()
    val lastMoodState by viewModel.lastMoodState.collectAsState()
    val weeklyStatsState by viewModel.weeklyStatsState.collectAsState()
    val dailyQuoteState by viewModel.dailyQuote.collectAsState()
    val refreshState = rememberPullToRefreshState()
    val isRefreshing = dailyQuoteState is DailyQuoteUiState.Loading ||
            lastMoodState is LastMoodUiState.Loading ||
            weeklyStatsState is WeeklyStatsUiState.Loading

    val backgroundColor = MaterialTheme.colorScheme.background
    PullToRefreshBox(
        modifier = Modifier.fillMaxWidth(),
        state = refreshState,
        isRefreshing = isRefreshing,
        onRefresh = {
            if (user.id.isNotEmpty()) viewModel.refreshData(user.id)
        },
        indicator = {
            Indicator(
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                color = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary,
            )
        }
    ) {
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (dailyQuoteState) {
                is DailyQuoteUiState.Loading -> DailyQuoteSkeleton()
                is DailyQuoteUiState.Success -> {
                    val quote = (dailyQuoteState as DailyQuoteUiState.Success).quote
                    DailyQuoteCard(
                        quote = quote,
                    )
                }
                is DailyQuoteUiState.Error -> DailyQuoteCard(quote = null)
                else -> {}
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (lastMoodState) {
                    is LastMoodUiState.Loading -> HomeMoodQuoteSkeleton()
                    is LastMoodUiState.Success -> {
                        val journal = (lastMoodState as LastMoodUiState.Success).journal

                        if (!journal.createdAt.isToday()) {
                            ReminderCard(
                                onRecordMoodClick = {
                                    navigate(Destinations.MoodEntry)
                                }
                            )
                        }

                        if (journal.mood.isEmpty()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                shape = CardDefaults.shape
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    text = stringResource(R.string.no_last_mood_description),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            MoodCard(
                                mood = journal.mood,
                                moodLevel = journal.moodLevel,
                                timestamp = journal.createdAt
                            )
                        }
                    }
                    is LastMoodUiState.Error -> {
                        val message = (lastMoodState as LastMoodUiState.Error).message

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            shape = CardDefaults.shape
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                text = message,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    else -> {}
                }
            }

            when (weeklyStatsState) {
                is WeeklyStatsUiState.Loading -> WeeklyStatsCardSkeleton()
                is WeeklyStatsUiState.Success -> {
                    val stats = (weeklyStatsState as WeeklyStatsUiState.Success).stats
                    WeeklyStatsCard(
                        weeklyStats = stats
                    )
                }
                is WeeklyStatsUiState.Error -> {
                    WeeklyStatsCard(
                        weeklyStats = null
                    )
                }
                else -> {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    ReflectTheme {
        HomeScreen()
    }
}