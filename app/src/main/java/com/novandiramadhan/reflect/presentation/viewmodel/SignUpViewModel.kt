package com.novandiramadhan.reflect.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.data.service.GoogleAuthService
import com.novandiramadhan.reflect.domain.datastore.UserDataStore
import com.novandiramadhan.reflect.domain.model.User
import com.novandiramadhan.reflect.domain.usecase.UserUseCase
import com.novandiramadhan.reflect.presentation.state.SignUpUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val userDataStore: UserDataStore,
    private val userUseCase: UserUseCase,
    private val googleAuthService: GoogleAuthService
): ViewModel() {
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Initial)
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun setName(name: String) {
        _name.value = name
    }

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun register() {
        if (name.value.isBlank() || email.value.isBlank() || password.value.isBlank()) {
            _uiState.value = SignUpUiState.Error(
                context.getString(R.string.fields_empty)
            )
            return
        }

        viewModelScope.launch {
            val user = User(
                name = name.value,
                email = email.value
            )
            userUseCase.register(user, password.value).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.value = SignUpUiState.Loading
                    is Resource.Success -> {
                        result.data?.let { user ->
                            _uiState.value = SignUpUiState.Success(user)
                        }
                    }
                    is Resource.Error -> {
                        _uiState.value = SignUpUiState.Error(
                            result.message ?: context.getString(R.string.register_failed)
                        )
                    }
                }
            }
        }
    }

    fun getTokenGoogleAuth() {
        viewModelScope.launch {
            googleAuthService.signIn()
                .fold(
                    onSuccess = { token ->
                        signInWithGoogle(token)
                    },
                    onFailure = { error ->
                        _uiState.value = SignUpUiState.Error(
                            error.message ?: context.getString(R.string.google_signin_failed)
                        )
                    }
                )
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            userUseCase.signInWithGoogle(idToken).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.value = SignUpUiState.Loading
                    is Resource.Success -> {
                        result.data?.let { user ->
                            setUserState(user)
                            _uiState.value = SignUpUiState.Success(user)
                        }
                    }
                    is Resource.Error -> {
                        _uiState.value = SignUpUiState.Error(
                            result.message ?: context.getString(R.string.google_signin_failed)
                        )
                    }
                }
            }
        }
    }

    fun clearState() {
        _uiState.update { SignUpUiState.Initial }
    }

    fun setUserState(user: User) {
        viewModelScope.launch {
            userDataStore.setState(user)
        }
    }
}