package com.novandiramadhan.reflect.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.domain.datastore.UserDataStore
import com.novandiramadhan.reflect.domain.model.User
import com.novandiramadhan.reflect.domain.usecase.MoodUseCase
import com.novandiramadhan.reflect.presentation.state.AverageMoodUiState
import com.novandiramadhan.reflect.presentation.state.EntryStreakUiState
import com.novandiramadhan.reflect.presentation.state.MoodTrendUiState
import com.novandiramadhan.reflect.presentation.state.MostFrequentMoodUiState
import com.novandiramadhan.reflect.presentation.state.TopTriggersUiState
import com.novandiramadhan.reflect.presentation.state.component.MoodTrendChartState
import com.novandiramadhan.reflect.util.generateWeeksForYear
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class InsightViewModel @Inject constructor(
    userDataStore: UserDataStore,
    private val moodUseCase: MoodUseCase
): ViewModel() {
    val user: StateFlow<User> = userDataStore.state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(2000),
        initialValue = User()
    )

    private val _moodTrendChartState = MutableStateFlow(MoodTrendChartState())
    val moodTrendChartState: StateFlow<MoodTrendChartState> = _moodTrendChartState.asStateFlow()

    private val _averageMood = MutableStateFlow<AverageMoodUiState>(AverageMoodUiState.Idle)
    val averageMood: StateFlow<AverageMoodUiState> = _averageMood.asStateFlow()

    private val _moodTrend = MutableStateFlow<MoodTrendUiState>(MoodTrendUiState.Idle)
    val moodTrend: StateFlow<MoodTrendUiState> = _moodTrend.asStateFlow()

    private val _mostFrequentMood = MutableStateFlow<MostFrequentMoodUiState>(
        MostFrequentMoodUiState.Idle)
    val mostFrequentMood: StateFlow<MostFrequentMoodUiState> = _mostFrequentMood.asStateFlow()

    private val _entryStreak = MutableStateFlow<EntryStreakUiState>(EntryStreakUiState.Idle)
    val entryStreak: StateFlow<EntryStreakUiState> = _entryStreak.asStateFlow()

    private val _topTriggers = MutableStateFlow<TopTriggersUiState>(TopTriggersUiState.Idle)
    val topTriggers: StateFlow<TopTriggersUiState> = _topTriggers.asStateFlow()

    init {
        initMoodTrendChartState()
        viewModelScope.launch {
            user.collect { userData ->
                if (userData.id.isNotEmpty()) {
                    getAverageMoodInWeek(userData.id)
                    getMoodTrendInWeek(userData.id)
                    getMostFrequentMoodInWeek(userData.id)
                    getEntryStreakDays(userData.id)
                    getTopTriggers(userData.id)
                }
            }
        }
    }

    fun getAverageMoodInWeek(userId: String) {
        viewModelScope.launch {
            moodUseCase.getAverageMoodInWeek(userId)
                .catch { e ->
                    _averageMood.value = AverageMoodUiState.Error(e.message.orEmpty())
                }
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _averageMood.value = AverageMoodUiState.Loading
                        }
                        is Resource.Success -> {
                            _averageMood.value = AverageMoodUiState.Success(resource.data)
                        }
                        is Resource.Error -> {
                            _averageMood.value = AverageMoodUiState.Error(resource.message.orEmpty())
                        }
                    }
            }
        }
    }

    fun getMoodTrendInWeek(userId: String) {
        viewModelScope.launch {
            val state = _moodTrendChartState.value
            val selectedWeek = state.weeksInYear[state.selectedWeekIndex]

            moodUseCase.getMoodTrendInWeek(userId, selectedWeek)
                .catch { e ->
                    _moodTrend.value = MoodTrendUiState.Error(e.message.orEmpty())
                }
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _moodTrend.value = MoodTrendUiState.Loading
                        }
                        is Resource.Success -> {
                            _moodTrend.value = MoodTrendUiState.Success(resource.data ?: emptyList())
                        }
                        is Resource.Error -> {
                            _moodTrend.value = MoodTrendUiState.Error(resource.message.orEmpty())
                        }
                    }
                }
        }
    }

    fun getMostFrequentMoodInWeek(userId: String) {
        viewModelScope.launch {
            moodUseCase.getMostFrequentMoodInWeek(userId)
                .catch { e ->
                    _mostFrequentMood.value = MostFrequentMoodUiState.Error(e.message.orEmpty())
                }
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _mostFrequentMood.value = MostFrequentMoodUiState.Loading
                        }
                        is Resource.Success -> {
                            _mostFrequentMood.value = MostFrequentMoodUiState.Success(resource.data)
                        }
                        is Resource.Error -> {
                            _mostFrequentMood.value = MostFrequentMoodUiState.Error(resource.message.orEmpty())
                        }
                    }
            }
        }
    }

    fun getEntryStreakDays(userId: String) {
        viewModelScope.launch {
            moodUseCase.getEntryStreakInWeek(userId)
                .catch { e ->
                    _entryStreak.value = EntryStreakUiState.Error(e.message.orEmpty())
                }
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _entryStreak.value = EntryStreakUiState.Loading
                        }
                        is Resource.Success -> {
                            _entryStreak.value = EntryStreakUiState.Success(resource.data)
                        }
                        is Resource.Error -> {
                            _entryStreak.value = EntryStreakUiState.Error(resource.message.orEmpty())
                        }
                    }
                }
        }
    }

    fun getTopTriggers(userId: String) {
        viewModelScope.launch {
            moodUseCase.getTopTriggersInWeek(userId)
                .catch { e ->
                    _topTriggers.value = TopTriggersUiState.Error(e.message.orEmpty())
                }
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _topTriggers.value = TopTriggersUiState.Loading
                        }
                        is Resource.Success -> {
                            _topTriggers.value = TopTriggersUiState.Success(resource.data!!)
                        }
                        is Resource.Error -> {
                            _topTriggers.value = TopTriggersUiState.Error(resource.message.orEmpty())
                        }
                    }
                }
        }
    }

    private fun initMoodTrendChartState() {
        val currentCalendar = Calendar.getInstance()
        val currentYear = currentCalendar.get(Calendar.YEAR)
        val weeksInYear = generateWeeksForYear(currentYear)
        val today = Calendar.getInstance().time

        val currentWeekIndex = weeksInYear.indexOfFirst { week ->
            today.time >= week.startDate.time && today.time <= week.endDate.time
        }.coerceAtLeast(0)

        _moodTrendChartState.value = MoodTrendChartState(
            selectedWeekIndex = currentWeekIndex,
            selectedYear = currentYear,
            currentWeekIndex = currentWeekIndex,
            weeksInYear = weeksInYear
        )
    }

    fun updateSelectedWeek(index: Int) {
        if (index != _moodTrendChartState.value.selectedWeekIndex) {
            _moodTrendChartState.value = _moodTrendChartState.value.copy(
                selectedWeekIndex = index,
                showWeekDropdown = false
            )
            if (user.value.id.isNotEmpty()) {
                getMoodTrendInWeek(user.value.id)
            }
        } else {
            _moodTrendChartState.value = _moodTrendChartState.value.copy(
                showWeekDropdown = false
            )
        }
    }

    fun updateSelectedYear(year: Int) {
        val state = _moodTrendChartState.value
        val weeksInYear = generateWeeksForYear(year)

        _moodTrendChartState.value = state.copy(
            selectedYear = year,
            weeksInYear = weeksInYear,
            selectedWeekIndex = if (year == state.selectedYear) state.selectedWeekIndex else 0,
            showYearDropdown = false
        )
        if (user.value.id.isNotEmpty()) {
            getMoodTrendInWeek(user.value.id)
        }
    }

    fun toggleWeekDropdown(show: Boolean) {
        _moodTrendChartState.value = _moodTrendChartState.value.copy(
            showWeekDropdown = show
        )
    }

    fun toggleYearDropdown(show: Boolean) {
        _moodTrendChartState.value = _moodTrendChartState.value.copy(
            showYearDropdown = show
        )
    }
}