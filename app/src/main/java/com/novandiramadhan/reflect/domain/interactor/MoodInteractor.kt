package com.novandiramadhan.reflect.domain.interactor

import com.google.firebase.Timestamp
import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.domain.model.Journal
import com.novandiramadhan.reflect.domain.model.MoodTrendData
import com.novandiramadhan.reflect.domain.model.MostFrequentMood
import com.novandiramadhan.reflect.domain.model.Quote
import com.novandiramadhan.reflect.domain.model.WeekData
import com.novandiramadhan.reflect.domain.model.WeeklyStats
import com.novandiramadhan.reflect.domain.repository.MoodRepository
import com.novandiramadhan.reflect.domain.usecase.MoodUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoodInteractor @Inject constructor(
    private val moodRepository: MoodRepository
): MoodUseCase {
    override fun addJournal(
        journal: Journal,
        userId: String
    ): Flow<Resource<String>> = moodRepository.addJournal(journal, userId)

    override fun getLastMood(userId: String): Flow<Resource<Journal>> =
        moodRepository.getLastMood(userId)

    override fun getWeeklyStats(
        userId: String,
        rangeDate: Pair<Timestamp, Timestamp>
    ): Flow<Resource<WeeklyStats?>> = moodRepository.getWeeklyStats(userId, rangeDate)

    override fun getDailyQuote(): Flow<Resource<Quote>> = moodRepository.getDailyQuote()

    override fun getAverageMoodInWeek(userId: String): Flow<Resource<Double?>> =
        moodRepository.getAverageMoodInWeek(userId)

    override fun getMostFrequentMoodInWeek(userId: String): Flow<Resource<MostFrequentMood?>> =
        moodRepository.getMostFrequentMoodInWeek(userId)

    override fun getMoodTrendInWeek(
        userId: String,
        week: WeekData
    ): Flow<Resource<List<MoodTrendData>>> = moodRepository.getMoodTrendInWeek(userId, week)

    override fun getEntryStreakInWeek(userId: String): Flow<Resource<Int>> =
        moodRepository.getEntryStreakInWeek(userId)

    override fun getTopTriggersInWeek(userId: String): Flow<Resource<List<String>>> =
        moodRepository.getTopTriggersInWeek(userId)
}