package com.novandiramadhan.reflect.presentation.state

import com.novandiramadhan.reflect.data.local.locale.AppLocale
import com.novandiramadhan.reflect.domain.model.Language

sealed class SettingUiState {
    data object Loading : SettingUiState()

    data class Success(
        val selectedLanguage: String = AppLocale.languages.first().code,
        val availableLanguages: List<Language> = emptyList(),
        val shouldRecreateActivity: Boolean = false
    ) : SettingUiState()

    data class Error(
        val message: String,
        val selectedLanguage: String = AppLocale.languages.first().code,
        val availableLanguages: List<Language> = emptyList()
    ) : SettingUiState()
}