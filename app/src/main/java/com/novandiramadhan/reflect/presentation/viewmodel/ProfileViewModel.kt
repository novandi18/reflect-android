package com.novandiramadhan.reflect.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novandiramadhan.reflect.data.local.locale.AppLocale
import com.novandiramadhan.reflect.data.local.locale.AppLocaleManager
import com.novandiramadhan.reflect.domain.datastore.SettingDataStore
import com.novandiramadhan.reflect.domain.datastore.UserDataStore
import com.novandiramadhan.reflect.domain.model.Setting
import com.novandiramadhan.reflect.domain.model.User
import com.novandiramadhan.reflect.domain.usecase.UserUseCase
import com.novandiramadhan.reflect.notification.scheduler.NotificationScheduler
import com.novandiramadhan.reflect.presentation.state.SettingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val userUseCase: UserUseCase,
    private val userDataStore: UserDataStore,
    private val settingDataStore: SettingDataStore,
    private val notificationScheduler: NotificationScheduler
): ViewModel() {
    private val appLocaleManager = AppLocaleManager()

    val user: StateFlow<User> = userDataStore.state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(2000),
        initialValue = User()
    )

    val settings: StateFlow<Setting> = settingDataStore.state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(2000),
        initialValue = Setting()
    )

    private val _showTimePicker = MutableStateFlow(false)
    val showTimePicker: StateFlow<Boolean> = _showTimePicker

    private val _showSignOutDialog = MutableStateFlow(false)
    val showSignOutDialog: StateFlow<Boolean> = _showSignOutDialog

    private val _settingState = MutableStateFlow<SettingUiState>(SettingUiState.Loading)
    val settingState: StateFlow<SettingUiState> = _settingState

    init {
        loadInitialLanguage()
    }

    private fun loadInitialLanguage() {
        try {
            val currentLanguage = appLocaleManager.getLanguageCode(context)
            _settingState.value = SettingUiState.Success(
                selectedLanguage = currentLanguage,
                availableLanguages = AppLocale.languages
            )
        } catch (e: Exception) {
            _settingState.value = SettingUiState.Error(
                message = e.message ?: "Unknown error",
                availableLanguages = AppLocale.languages
            )
        }
    }

    fun setShowTimePicker(show: Boolean) {
        _showTimePicker.value = show
    }

    fun setShowSignOutDialog(show: Boolean) {
        _showSignOutDialog.value = show
    }

    fun setLanguageSetting(lang: String) {
        viewModelScope.launch {
            try {
                // Update state untuk trigger recreation
                when (val currentState = _settingState.value) {
                    is SettingUiState.Success -> {
                        _settingState.value = currentState.copy(
                            selectedLanguage = lang,
                            shouldRecreateActivity = true
                        )
                    }
                    is SettingUiState.Error -> {
                        _settingState.value = SettingUiState.Success(
                            selectedLanguage = lang,
                            availableLanguages = AppLocale.languages,
                            shouldRecreateActivity = true
                        )
                    }
                    else -> {
                        _settingState.value = SettingUiState.Success(
                            selectedLanguage = lang,
                            availableLanguages = AppLocale.languages,
                            shouldRecreateActivity = true
                        )
                    }
                }

                // Save to datastore
                settingDataStore.setState(
                    settings.value.copy(language = lang)
                )

                // Apply language change
                appLocaleManager.changeLanguage(context, lang)
            } catch (e: Exception) {
                _settingState.value = SettingUiState.Error(
                    message = e.message ?: "Failed to change language",
                    selectedLanguage = lang,
                    availableLanguages = AppLocale.languages
                )
            }
        }
    }

    fun setThemeSetting(theme: String) {
        viewModelScope.launch {
            settingDataStore.setState(
                settings.value.copy(
                    theme = theme
                )
            )
        }
    }

    fun setBiometricSetting(useBiometric: Boolean) {
        viewModelScope.launch {
            settingDataStore.setState(
                settings.value.copy(
                    useBiometric = useBiometric
                )
            )
        }
    }

    fun onActivityRecreated() {
        when (val currentState = _settingState.value) {
            is SettingUiState.Success -> {
                _settingState.value = currentState.copy(shouldRecreateActivity = false)
            }
            else -> {}
        }
    }

    fun logout() {
        viewModelScope.launch {
            userUseCase.logout()
            userDataStore.deleteState()
            settingDataStore.deleteState()
            notificationScheduler.cancelDailyReminder()
        }
    }

    fun setDailyReminder(hour: Int, minute: Int) {
        viewModelScope.launch {
            notificationScheduler.scheduleDailyReminder(hour, minute)
            settingDataStore.setState(
                settings.value.copy(
                    reminderTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute),
                )
            )
        }
    }

    fun cancelDailyReminder() {
        viewModelScope.launch {
            notificationScheduler.cancelDailyReminder()
            settingDataStore.setState(
                settings.value.copy(
                    reminderTime = "",
                )
            )
        }
    }
}