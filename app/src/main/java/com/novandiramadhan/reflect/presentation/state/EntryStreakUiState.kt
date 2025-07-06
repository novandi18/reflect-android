package com.novandiramadhan.reflect.presentation.state

sealed class EntryStreakUiState {
    object Idle : EntryStreakUiState()
    object Loading : EntryStreakUiState()
    data class Success(val totalDays: Int?) : EntryStreakUiState()
    data class Error(val message: String) : EntryStreakUiState()
}