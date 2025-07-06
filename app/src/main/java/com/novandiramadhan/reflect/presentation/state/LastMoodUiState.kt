package com.novandiramadhan.reflect.presentation.state

import com.novandiramadhan.reflect.domain.model.Journal

sealed class LastMoodUiState {
    object Idle : LastMoodUiState()
    object Loading : LastMoodUiState()
    data class Success(val journal: Journal) : LastMoodUiState()
    data class Error(val message: String) : LastMoodUiState()
}