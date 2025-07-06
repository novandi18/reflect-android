package com.novandiramadhan.reflect.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.domain.datastore.UserDataStore
import com.novandiramadhan.reflect.domain.model.Journal
import com.novandiramadhan.reflect.domain.model.User
import com.novandiramadhan.reflect.domain.usecase.MoodUseCase
import com.novandiramadhan.reflect.presentation.state.JournalEntryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoodEntryViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val moodUseCase: MoodUseCase,
    userDataStore: UserDataStore
): ViewModel() {
    val user: StateFlow<User> = userDataStore.state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(2000),
        initialValue = User()
    )

    private val _triggers = MutableStateFlow<List<String>>(
        context.resources.getStringArray(R.array.mood_triggers).toList()
    )
    val triggers: StateFlow<List<String>> = _triggers

    private val _form = MutableStateFlow(Journal())
    val form: StateFlow<Journal> = _form

    private val _searchTriggerName = MutableStateFlow<String>("")
    val searchTriggerName: StateFlow<String> = _searchTriggerName

    private val _uiState = MutableStateFlow<JournalEntryUiState>(JournalEntryUiState.Idle)
    val uiState: StateFlow<JournalEntryUiState> = _uiState

    fun updateMood(mood: String) {
        _form.value = _form.value.copy(mood = mood)
    }

    fun updateMoodLevel(level: Int) {
        _form.value = _form.value.copy(moodLevel = level)
    }

    fun updateTriggers(triggers: List<String>) {
        _form.value = _form.value.copy(triggers = triggers)
    }

    fun updateTags(tags: List<String>) {
        _form.value = _form.value.copy(tags = tags)
    }

    fun updateNote(note: String) {
        _form.value = _form.value.copy(note = note)
    }

    fun updateSearchTriggerName(name: String) {
        _searchTriggerName.value = name
    }

    fun filterTriggers() {
        val searchQuery = _searchTriggerName.value
        if (searchQuery.isEmpty()) {
            _triggers.value = context.resources.getStringArray(R.array.mood_triggers).toList()
        } else {
            _triggers.value = context.resources.getStringArray(R.array.mood_triggers)
                .filter { it.contains(searchQuery, ignoreCase = true) }
        }
    }

    fun validateForm(): Boolean {
        return _form.value.triggers.size >= 3 && _form.value.mood.isNotEmpty() && _form.value.moodLevel > 0
    }

    fun addJournal(userId: String) {
        if (!validateForm()) {
            _uiState.value = JournalEntryUiState.Error(
                context.getString(R.string.error_mood_entry_fields)
            )
            return
        }

        viewModelScope.launch {
            if (userId.isEmpty()) {
                _uiState.value = JournalEntryUiState.Error(
                    context.getString(R.string.user_not_authenticated)
                )
                return@launch
            }

            moodUseCase.addJournal(form.value, userId)
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> _uiState.value = JournalEntryUiState.Loading
                        is Resource.Success -> {
                            result.data?.let { message ->
                                _uiState.value = JournalEntryUiState.Success(message)
                                resetForm()
                            }
                        }
                        is Resource.Error -> {
                            result.message?.let { message ->
                                _uiState.value = JournalEntryUiState.Error(message)
                            }
                        }
                    }
                }
        }
    }

    fun resetForm() {
        _form.value = Journal()
    }

    fun resetState() {
        _uiState.value = JournalEntryUiState.Idle
    }
}