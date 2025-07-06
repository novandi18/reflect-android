package com.novandiramadhan.reflect.domain.usecase

import com.google.firebase.Timestamp
import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.domain.model.Journal
import com.novandiramadhan.reflect.domain.model.MoodTrendData
import com.novandiramadhan.reflect.domain.model.MostFrequentMood
import com.novandiramadhan.reflect.domain.model.Quote
import com.novandiramadhan.reflect.domain.model.WeekData
import com.novandiramadhan.reflect.domain.model.WeeklyStats
import kotlinx.coroutines.flow.Flow

interface MoodUseCase {
    fun addJournal(journal: Journal, userId: String): Flow<Resource<String>>
    fun getLastMood(userId: String): Flow<Resource<Journal>>
    fun getWeeklyStats(
        userId: String, rangeDate: Pair<Timestamp, Timestamp>
    ): Flow<Resource<WeeklyStats?>>
    fun getDailyQuote(): Flow<Resource<Quote>>
    fun getAverageMoodInWeek(userId: String): Flow<Resource<Double?>>
    fun getMostFrequentMoodInWeek(userId: String): Flow<Resource<MostFrequentMood?>>
    fun getMoodTrendInWeek(userId: String, week: WeekData): Flow<Resource<List<MoodTrendData>>>
    fun getEntryStreakInWeek(userId: String): Flow<Resource<Int>>
    fun getTopTriggersInWeek(userId: String): Flow<Resource<List<String>>>
}