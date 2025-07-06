package com.novandiramadhan.reflect.presentation.state

import com.novandiramadhan.reflect.domain.model.User

sealed class SignUpUiState {
    object Initial : SignUpUiState()
    object Loading : SignUpUiState()
    data class Success(val user: User) : SignUpUiState()
    data class Error(val message: String) : SignUpUiState()
}