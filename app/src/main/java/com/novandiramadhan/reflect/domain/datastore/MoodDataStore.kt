package com.novandiramadhan.reflect.domain.datastore

import com.novandiramadhan.reflect.domain.model.MostFrequentMood
import com.novandiramadhan.reflect.domain.model.Quote
import kotlinx.coroutines.flow.Flow

interface MoodDataStore {
    val dailyQuote: Flow<Quote>
    val averageMoodLevel: Flow<Double>
    val mostFrequentMood: Flow<MostFrequentMood>
    val entryStreakDays: Flow<Int>
    val topTriggers: Flow<List<String>>
    suspend fun setDailyQuote(quote: Quote)
    suspend fun setAverageMoodLevel(moodLevel: Double)
    suspend fun setMostFrequentMood(mostFrequentMood: MostFrequentMood)
    suspend fun setEntryStreakDays(days: Int)
    suspend fun setTopTriggers(triggers: List<String>)
    suspend fun deleteDailyQuote()
    suspend fun deleteAverageMoodLevel()
    suspend fun deleteMostFrequentMood()
    suspend fun deleteEntryStreakDays()
    suspend fun deleteTopTriggers()
}