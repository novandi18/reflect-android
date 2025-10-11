package com.novandiramadhan.reflect.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.data.local.room.entity.NotificationEntity
import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.domain.datastore.UserDataStore
import com.novandiramadhan.reflect.domain.model.User
import com.novandiramadhan.reflect.domain.usecase.NotificationUseCase
import com.novandiramadhan.reflect.presentation.state.NotificationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    userDataStore: UserDataStore,
    private val notificationUseCase: NotificationUseCase
) : ViewModel() {
    private val _notificationState = MutableStateFlow<NotificationUiState>(NotificationUiState.Idle)
    val notificationState: StateFlow<NotificationUiState> = _notificationState.asStateFlow()

    private val _notifications = MutableStateFlow<PagingData<NotificationEntity>>(PagingData.empty())
    val notifications: StateFlow<PagingData<NotificationEntity>> = _notifications.asStateFlow()

    val user: StateFlow<User> = userDataStore.state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(2000),
        initialValue = User()
    )

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            user.collect { userData ->
                if (userData.id.isNotEmpty()) {
                    getAllNotifications(userData.id)
                }
            }
        }
    }

    private fun getAllNotifications(userId: String) {
        viewModelScope.launch {
            notificationUseCase.getAllNotifications(userId)
                .cachedIn(viewModelScope)
                .catch {
                    _notificationState.value = NotificationUiState.Error(
                        it.message ?: context.getString(R.string.generic_error)
                    )
                }
                .collect { pagingData ->
                    _notifications.value = pagingData
                }
        }
    }

    fun deleteNotification(userId: String, notificationId: String) {
        _notificationState.value = NotificationUiState.Loading
        viewModelScope.launch {
            notificationUseCase.deleteNotification(userId, notificationId)
                .catch {
                    _notificationState.value = NotificationUiState.Error(
                        it.message ?: context.getString(R.string.generic_error)
                    )
                }
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> _notificationState.value = NotificationUiState.Loading
                        is Resource.Success -> _notificationState.value = NotificationUiState.Success(result.data)
                        is Resource.Error -> _notificationState.value = NotificationUiState.Error(
                            result.message ?: context.getString(R.string.generic_error)
                        )
                    }
                }
        }
    }

    fun deleteAllNotifications() {
        _notificationState.value = NotificationUiState.Loading
        viewModelScope.launch {
            val userId = user.value.id
            if (userId.isNotEmpty()) {
                notificationUseCase.deleteAllNotifications(userId)
                    .catch {
                        _notificationState.value = NotificationUiState.Error(
                            it.message ?: context.getString(R.string.generic_error)
                        )
                    }
                    .collect { result ->
                        when (result) {
                            is Resource.Loading -> _notificationState.value = NotificationUiState.Loading
                            is Resource.Success -> _notificationState.value = NotificationUiState.Success(result.data)
                            is Resource.Error -> _notificationState.value = NotificationUiState.Error(
                                result.message ?: context.getString(R.string.generic_error)
                            )
                        }
                    }
            } else {
                _notificationState.value = NotificationUiState.Error(context.getString(R.string.generic_error))
            }
        }
    }

    fun markAsRead(userId: String, notificationId: String) {
        _notificationState.value = NotificationUiState.Loading
        viewModelScope.launch {
            notificationUseCase.markAsRead(userId, notificationId)
                .catch {
                    _notificationState.value = NotificationUiState.Error(
                        it.message ?: context.getString(R.string.generic_error)
                    )
                }
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> _notificationState.value = NotificationUiState.Loading
                        is Resource.Success -> _notificationState.value = NotificationUiState.Success(result.data)
                        is Resource.Error -> _notificationState.value = NotificationUiState.Error(
                            result.message ?: context.getString(R.string.generic_error)
                        )
                    }
                }
        }
    }

    fun markAllAsRead() {
        _notificationState.value = NotificationUiState.Loading
        viewModelScope.launch {
            val userId = user.value.id
            if (userId.isNotEmpty()) {
                notificationUseCase.markAllAsRead(userId)
                    .catch {
                        _notificationState.value = NotificationUiState.Error(
                            it.message ?: context.getString(R.string.generic_error)
                        )
                    }
                    .collect { result ->
                        when (result) {
                            is Resource.Loading -> _notificationState.value = NotificationUiState.Loading
                            is Resource.Success -> _notificationState.value = NotificationUiState.Success(result.data)
                            is Resource.Error -> _notificationState.value = NotificationUiState.Error(
                                result.message ?: context.getString(R.string.generic_error)
                            )
                        }
                    }
            } else {
                _notificationState.value = NotificationUiState.Error(context.getString(R.string.generic_error))
            }
        }
    }

    fun refreshNotifications(userId: String) {
        _notificationState.value = NotificationUiState.Loading

        if (userId.isNotEmpty()) {
            getAllNotifications(userId)
        } else {
            _notificationState.value = NotificationUiState.Error(context.getString(R.string.generic_error))
        }
    }
}