package com.novandiramadhan.reflect.domain.datastore

import kotlinx.coroutines.flow.Flow

interface WelcomeDataStore {
    val state: Flow<Boolean>
    suspend fun setState(isWelcome: Boolean)
}