package com.novandiramadhan.reflect.domain.datastore

import com.novandiramadhan.reflect.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserDataStore {
    val state: Flow<User>
    suspend fun setState(state: User)
    suspend fun deleteState()
}