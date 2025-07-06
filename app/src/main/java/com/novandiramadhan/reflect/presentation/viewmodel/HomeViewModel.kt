package com.novandiramadhan.reflect.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.domain.datastore.UserDataStore
import com.novandiramadhan.reflect.domain.model.User
import com.novandiramadhan.reflect.domain.usecase.MoodUseCase
import com.novandiramadhan.reflect.presentation.state.DailyQuoteUiState
import com.novandiramadhan.reflect.presentation.state.LastMoodUiState
import com.novandiramadhan.reflect.presentation.state.WeeklyStatsUiState
import com.novandiramadhan.reflect.util.getCurrentWeekRange
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
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val moodUseCase: MoodUseCase,
    userDataStore: UserDataStore
): ViewModel() {
    private val _lastMoodState = MutableStateFlow<LastMoodUiState>(LastMoodUiState.Idle)
    val lastMoodState: StateFlow<LastMoodUiState> = _lastMoodState.asStateFlow()

    private val _weeklyStatsState = MutableStateFlow<WeeklyStatsUiState>(WeeklyStatsUiState.Idle)
    val weeklyStatsState: StateFlow<WeeklyStatsUiState> = _weeklyStatsState.asStateFlow()

    private val _dailyQuote = MutableStateFlow<DailyQuoteUiState>(DailyQuoteUiState.Idle)
    val dailyQuote: StateFlow<DailyQuoteUiState> = _dailyQuote.asStateFlow()

    val user: StateFlow<User> = userDataStore.state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(2000),
        initialValue = User()
    )

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        getDailyQuote()
        viewModelScope.launch {
            user.collect { userData ->
                if (userData.id.isNotEmpty()) {
                    fetchLastMood(userData.id)
                    fetchWeeklyStats(userData.id)
                }
            }
        }
    }

    private fun fetchLastMood(userId: String) {
        if (userId.isEmpty()) {
            _lastMoodState.value = LastMoodUiState.Error(
                context.getString(R.string.user_not_authenticated)
            )
            return
        }
        viewModelScope.launch {
            moodUseCase.getLastMood(userId)
                .catch { e ->
                    _lastMoodState.value = LastMoodUiState.Error(
                        e.message ?: context.getString(R.string.generic_error)
                    )
                }
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> _lastMoodState.value = LastMoodUiState.Loading
                        is Resource.Success -> {
                            val journal = result.data
                            if (journal != null) {
                                _lastMoodState.value = LastMoodUiState.Success(journal)
                            } else {
                                _lastMoodState.value = LastMoodUiState.Error(
                                    context.getString(R.string.no_last_mood_description)
                                )
                            }
                        }
                        is Resource.Error -> _lastMoodState.value = LastMoodUiState.Error(
                            result.message ?: context.getString(R.string.generic_error)
                        )
                    }
            }
        }
    }

    private fun fetchWeeklyStats(userId: String) {
        if (userId.isEmpty()) {
            _weeklyStatsState.value = WeeklyStatsUiState.Error(
                context.getString(R.string.user_not_authenticated)
            )
            return
        }
        viewModelScope.launch {
            val currentWeek = getCurrentWeekRange()
            val startTimestamp = Timestamp(currentWeek.first)
            val endTimestamp = Timestamp(currentWeek.second)

            moodUseCase.getWeeklyStats(userId, startTimestamp to endTimestamp)
                .catch { e ->
                    _weeklyStatsState.value = WeeklyStatsUiState.Error(
                        e.message ?: context.getString(R.string.generic_error)
                    )
                }
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> _weeklyStatsState.value = WeeklyStatsUiState.Loading
                        is Resource.Success -> {
                            val stats = result.data
                            if (stats != null) {
                                _weeklyStatsState.value = WeeklyStatsUiState.Success(stats)
                            } else {
                                _weeklyStatsState.value = WeeklyStatsUiState.Error(
                                    context.getString(R.string.error_weekly_stats_not_found)
                                )
                            }
                        }
                        is Resource.Error -> _weeklyStatsState.value = WeeklyStatsUiState.Error(
                            result.message ?: context.getString(R.string.generic_error)
                        )
                    }
            }
        }
    }

    private fun getDailyQuote() {
        viewModelScope.launch {
            moodUseCase.getDailyQuote()
                .catch { e ->
                    _dailyQuote.value = DailyQuoteUiState.Error(
                        e.message ?: context.getString(R.string.generic_error)
                    )
                }
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> _dailyQuote.value = DailyQuoteUiState.Loading
                        is Resource.Success -> {
                            val quote = result.data
                            if (quote != null) {
                                _dailyQuote.value = DailyQuoteUiState.Success(quote)
                            } else {
                                _dailyQuote.value = DailyQuoteUiState.Error(
                                    context.getString(R.string.no_quote_description)
                                )
                            }
                        }
                        is Resource.Error -> _dailyQuote.value = DailyQuoteUiState.Error(
                            result.message ?: context.getString(R.string.generic_error)
                        )
                    }
                }
        }
    }

    fun refreshData(userId: String) {
        _dailyQuote.value = DailyQuoteUiState.Loading
        if (userId.isNotEmpty()) {
            _lastMoodState.value = LastMoodUiState.Loading
            _weeklyStatsState.value = WeeklyStatsUiState.Loading
        }

        getDailyQuote()
        if (userId.isNotEmpty()) {
            fetchLastMood(userId)
            fetchWeeklyStats(userId)
        }
    }
}