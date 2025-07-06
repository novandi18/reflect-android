package com.novandiramadhan.reflect.presentation.state

import com.novandiramadhan.reflect.domain.model.User

sealed class SignInUiState {
    object Initial : SignInUiState()
    object Loading : SignInUiState()
    data class Success(val user: User) : SignInUiState()
    data class Error(val message: String) : SignInUiState()
}