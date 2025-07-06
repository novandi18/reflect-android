package com.novandiramadhan.reflect.presentation.state

import com.novandiramadhan.reflect.domain.model.MoodTrendData

sealed class MoodTrendChartUiState {
    object Idle : MoodTrendChartUiState()
    object Loading : MoodTrendChartUiState()
    data class Success(val data: List<MoodTrendData>) : MoodTrendChartUiState()
    data class Error(val message: String) : MoodTrendChartUiState()
}