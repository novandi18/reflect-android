package com.novandiramadhan.reflect.domain.datastore

import com.novandiramadhan.reflect.domain.model.Setting
import kotlinx.coroutines.flow.Flow

interface SettingDataStore {
    val state: Flow<Setting>
    suspend fun setState(state: Setting)
    suspend fun deleteState()
}