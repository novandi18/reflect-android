package com.novandiramadhan.reflect.presentation.state

sealed class NotificationUiState {
    object Idle : NotificationUiState()
    object Loading : NotificationUiState()
    data class Success(val data: Any? = null) : NotificationUiState()
    data class Error(val message: String) : NotificationUiState()
}