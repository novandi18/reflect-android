package com.novandiramadhan.reflect.presentation.state

import com.novandiramadhan.reflect.domain.model.MoodTrendData

sealed class MoodTrendUiState {
    object Idle : MoodTrendUiState()
    object Loading : MoodTrendUiState()
    data class Success(val data: List<MoodTrendData>) : MoodTrendUiState()
    data class Error(val message: String) : MoodTrendUiState()
}