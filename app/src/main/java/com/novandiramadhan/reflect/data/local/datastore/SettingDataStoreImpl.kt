package com.novandiramadhan.reflect.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.novandiramadhan.reflect.domain.datastore.SettingDataStore
import com.novandiramadhan.reflect.domain.model.Setting
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingDataStoreImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
): SettingDataStore {
    override val state: Flow<Setting> = context.settingDataStore.data
        .catch { exception ->
            if (exception is Exception) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            Setting(
                reminderTime = preferences[SETTING_REMINDER_TIME] ?: "",
                theme = preferences[SETTING_THEME] ?: "light"
            )
        }

    override suspend fun setState(state: Setting) {
        context.settingDataStore.edit { preferences ->
            preferences[SETTING_REMINDER_TIME] = state.reminderTime
            preferences[SETTING_THEME] = state.theme
        }
    }

    override suspend fun deleteState() {
        context.settingDataStore.edit { preferences ->
            preferences.remove(SETTING_REMINDER_TIME)
            preferences.remove(SETTING_THEME)
            preferences.clear()
        }
    }

    companion object {
        private val Context.settingDataStore by preferencesDataStore(name = "setting_preference")
        private val SETTING_REMINDER_TIME = stringPreferencesKey(name = "setting_reminder_time")
        private val SETTING_THEME = stringPreferencesKey(name = "setting_theme")
    }
}