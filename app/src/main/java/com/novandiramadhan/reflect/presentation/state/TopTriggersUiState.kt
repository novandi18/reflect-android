package com.novandiramadhan.reflect.presentation.state

sealed class TopTriggersUiState {
    object Idle : TopTriggersUiState()
    object Loading : TopTriggersUiState()
    data class Success(val triggers: List<String>) : TopTriggersUiState()
    data class Error(val message: String) : TopTriggersUiState()
}