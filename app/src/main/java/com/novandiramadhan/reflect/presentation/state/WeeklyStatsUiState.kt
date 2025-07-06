package com.novandiramadhan.reflect.presentation.state

import com.novandiramadhan.reflect.domain.model.WeeklyStats

sealed class WeeklyStatsUiState {
    object Idle : WeeklyStatsUiState()
    object Loading : WeeklyStatsUiState()
    data class Success(val stats: WeeklyStats) : WeeklyStatsUiState()
    data class Error(val message: String) : WeeklyStatsUiState()
}