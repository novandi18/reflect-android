package com.novandiramadhan.reflect.presentation.state

import com.novandiramadhan.reflect.domain.model.MostFrequentMood

sealed class MostFrequentMoodUiState {
    object Idle : MostFrequentMoodUiState()
    object Loading : MostFrequentMoodUiState()
    data class Success(val data: MostFrequentMood?) : MostFrequentMoodUiState()
    data class Error(val message: String) : MostFrequentMoodUiState()
}