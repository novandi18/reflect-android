package com.novandiramadhan.reflect.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novandiramadhan.reflect.domain.datastore.WelcomeDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val welcomeDataStore: WelcomeDataStore
) : ViewModel() {
    fun setState(isWelcome: Boolean) {
        viewModelScope.launch {
            welcomeDataStore.setState(isWelcome)
        }
    }
}