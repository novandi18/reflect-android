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
import com.novandiramadhan.reflect.presentation.state.SignInUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val userDataStore: UserDataStore,
    private val userUseCase: UserUseCase,
    private val googleAuthService: GoogleAuthService
): ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _uiState = MutableStateFlow<SignInUiState>(SignInUiState.Initial)
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun login() {
        if (email.value.isBlank() || password.value.isBlank()) {
            _uiState.value = SignInUiState.Error(
                context.getString(R.string.fields_empty)
            )
            return
        }

        viewModelScope.launch {
            userUseCase.login(email.value, password.value)
                .catch { e ->
                    _uiState.value = SignInUiState.Error(
                        e.message ?: context.getString(R.string.login_failed)
                    )
                }
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> _uiState.value = SignInUiState.Loading
                        is Resource.Success -> {
                            result.data?.let { user ->
                                setUserState(user)
                                _uiState.value = SignInUiState.Success(user)
                            }
                        }
                        is Resource.Error -> {
                            _uiState.value = SignInUiState.Error(
                                result.message ?: context.getString(R.string.login_failed)
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
                        _uiState.value = SignInUiState.Error(
                            error.message ?: context.getString(R.string.google_signin_failed)
                        )
                    }
                )
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            userUseCase.signInWithGoogle(idToken)
                .catch { e ->
                    _uiState.value = SignInUiState.Error(
                        e.message ?: context.getString(R.string.google_signin_failed)
                    )
                }
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> _uiState.value = SignInUiState.Loading
                        is Resource.Success -> {
                            result.data?.let { user ->
                                setUserState(user)
                                _uiState.value = SignInUiState.Success(user)
                            }
                        }
                        is Resource.Error -> {
                            _uiState.value = SignInUiState.Error(
                                result.message ?: context.getString(R.string.google_signin_failed)
                            )
                        }
                    }
            }
        }
    }

    fun clearState() {
        _uiState.update { SignInUiState.Initial }
    }

    fun setUserState(user: User) {
        viewModelScope.launch {
            userDataStore.setState(user)
        }
    }
}