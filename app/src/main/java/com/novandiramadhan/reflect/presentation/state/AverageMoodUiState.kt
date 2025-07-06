package com.novandiramadhan.reflect.presentation.state

sealed class AverageMoodUiState {
    object Idle : AverageMoodUiState()
    object Loading : AverageMoodUiState()
    data class Success(val average: Double?) : AverageMoodUiState()
    data class Error(val message: String) : AverageMoodUiState()
}