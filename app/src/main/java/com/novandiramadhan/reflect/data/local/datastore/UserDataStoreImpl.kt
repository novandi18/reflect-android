package com.novandiramadhan.reflect.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.novandiramadhan.reflect.domain.datastore.UserDataStore
import com.novandiramadhan.reflect.domain.model.User
import com.novandiramadhan.reflect.util.formatTimestamp
import com.novandiramadhan.reflect.util.parseTimestamp
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataStoreImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
): UserDataStore {
    override val state: Flow<User> = context.userDataStore.data
        .catch { exception ->
            if (exception is Exception) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val timestampStr = preferences[USER_CREATED_AT]
            val timestamp = timestampStr?.let { parseTimestamp(it) }

            User(
                id = preferences[USER_ID] ?: "",
                name = preferences[USER_NAME] ?: "",
                email = preferences[USER_EMAIL] ?: "",
                createdAt = timestamp
            )
        }

    override suspend fun setState(state: User) {
        context.userDataStore.edit { preferences ->
            preferences[USER_ID] = state.id
            preferences[USER_NAME] = state.name
            preferences[USER_EMAIL] = state.email
            state.createdAt?.let {
                preferences[USER_CREATED_AT] = formatTimestamp(it)
            }
        }
    }

    override suspend fun deleteState() {
        context.userDataStore.edit { preferences ->
            preferences.remove(USER_ID)
            preferences.remove(USER_NAME)
            preferences.remove(USER_EMAIL)
            preferences.remove(USER_CREATED_AT)
            preferences.clear()
        }
    }

    companion object {
        private val Context.userDataStore by preferencesDataStore(name = "user_preference")
        private val USER_ID = stringPreferencesKey(name = "user_id")
        private val USER_NAME = stringPreferencesKey(name = "user_name")
        private val USER_EMAIL = stringPreferencesKey(name = "user_email")
        private val USER_CREATED_AT = stringPreferencesKey(name = "user_created_at")
    }
}