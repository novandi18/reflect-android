package com.novandiramadhan.reflect.data.repository

import android.content.Context
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.data.local.room.dao.MoodHistoryDao
import com.novandiramadhan.reflect.data.local.room.entity.MoodHistoryEntity
import com.novandiramadhan.reflect.data.remote.RemoteDataSource
import com.novandiramadhan.reflect.data.remote.response.ZenQuotesResponse
import com.novandiramadhan.reflect.data.resource.ApiResource
import com.novandiramadhan.reflect.data.resource.NetworkOnlyResource
import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.domain.datastore.MoodDataStore
import com.novandiramadhan.reflect.domain.model.Journal
import com.novandiramadhan.reflect.domain.model.MoodTrendData
import com.novandiramadhan.reflect.domain.model.MostFrequentMood
import com.novandiramadhan.reflect.domain.model.Quote
import com.novandiramadhan.reflect.domain.model.WeekData
import com.novandiramadhan.reflect.domain.model.WeeklyStats
import com.novandiramadhan.reflect.domain.repository.MoodRepository
import com.novandiramadhan.reflect.util.FirestoreCollections
import com.novandiramadhan.reflect.util.getCurrentWeekRange
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class MoodRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firestore: FirebaseFirestore,
    private val moodHistoryDao: MoodHistoryDao,
    private val remoteDataSource: RemoteDataSource,
    private val moodDataStore: MoodDataStore
): MoodRepository {
    override fun addJournal(
        journal: Journal,
        userId: String
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val journalRef = firestore.collection(FirestoreCollections.USERS)
                .document(userId)
                .collection(FirestoreCollections.SubCollections.JOURNAL_ENTRIES)
                .document()

            val documentId = journalRef.id

            journalRef.set(journal).await()

            val moodHistoryEntity = MoodHistoryEntity(
                documentId = documentId,
                mood = journal.mood,
                moodLevel = journal.moodLevel,
                triggers = journal.triggers,
                tags = journal.tags,
                note = journal.note,
                createdAt = journal.createdAt.toDate().time
            )

            moodHistoryDao.insertMoodHistory(moodHistoryEntity)

            emit(Resource.Success(context.getString(R.string.success_add_journal)))
        } catch (e: Exception) {
            e.printStackTrace()
            when (e) {
                is FirebaseFirestoreException -> {
                    when (e.code) {
                        FirebaseFirestoreException.Code.UNAVAILABLE,
                        FirebaseFirestoreException.Code.DEADLINE_EXCEEDED -> {
                            emit(Resource.Error(context.getString(R.string.error_network)))
                        }
                        FirebaseFirestoreException.Code.PERMISSION_DENIED -> {
                            emit(Resource.Error(context.getString(R.string.error_add_journal)))
                        }
                        FirebaseFirestoreException.Code.UNAUTHENTICATED -> {
                            emit(Resource.Error(context.getString(R.string.error_add_journal)))
                        }
                        else -> {
                            emit(Resource.Error(context.getString(R.string.error_add_journal)))
                        }
                    }
                }
                is FirebaseNetworkException -> {
                    emit(Resource.Error(context.getString(R.string.error_network)))
                }
                else -> {
                    when {
                        e.message?.contains("network") == true -> {
                            emit(Resource.Error(context.getString(R.string.error_network)))
                        }
                        else -> {
                            emit(Resource.Error(context.getString(R.string.error_add_journal)))
                        }
                    }
                }
            }
        }
    }

    override fun getLastMood(userId: String): Flow<Resource<Journal>> = flow {
        emit(Resource.Loading())
        try {
            val local = try {
                moodHistoryDao.getLastMoodHistory()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            if (local != null) {
                emit(Resource.Success(
                    Journal(
                        mood = local.mood,
                        moodLevel = local.moodLevel,
                        triggers = local.triggers,
                        tags = local.tags,
                        note = local.note,
                        createdAt = Timestamp(local.createdAt / 1000, ((local.createdAt % 1000) * 1000000).toInt())
                    )
                ))
                return@flow
            }

            try {
                val snapshot = firestore.collection(FirestoreCollections.USERS)
                    .document(userId)
                    .collection(FirestoreCollections.SubCollections.JOURNAL_ENTRIES)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .await()

                if (snapshot.isEmpty) {
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DAY_OF_YEAR, -1)
                    val yesterdayTimestamp = Timestamp(calendar.time)

                    val emptyJournal = Journal(
                        createdAt = yesterdayTimestamp
                    )
                    emit(Resource.Success(emptyJournal))
                    return@flow
                }

                val doc = snapshot.documents.firstOrNull()
                if (doc != null) {
                    val journal = doc.toObject(Journal::class.java)
                    if (journal != null) {
                        emit(Resource.Success(journal))
                    } else {
                        val calendar = Calendar.getInstance()
                        calendar.add(Calendar.DAY_OF_YEAR, -1)
                        val yesterdayTimestamp = Timestamp(calendar.time)

                        emit(Resource.Success(Journal(
                            createdAt = yesterdayTimestamp
                        )))
                    }
                } else {
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DAY_OF_YEAR, -1)
                    val yesterdayTimestamp = Timestamp(calendar.time)

                    emit(Resource.Success(Journal(
                        createdAt = yesterdayTimestamp
                    )))
                }
            } catch (e: Exception) {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                val yesterdayTimestamp = Timestamp(calendar.time)

                emit(Resource.Success(Journal(
                    createdAt = yesterdayTimestamp
                )))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val yesterdayTimestamp = Timestamp(calendar.time)

            emit(Resource.Success(Journal(
                createdAt = yesterdayTimestamp
            )))
        }
    }

    override fun getWeeklyStats(
        userId: String,
        rangeDate: Pair<Timestamp, Timestamp>
    ): Flow<Resource<WeeklyStats?>> = flow {
        emit(Resource.Loading())
        try {
            val localList = try {
                moodHistoryDao.getMoodHistoryInRange(
                    start = rangeDate.first.toDate().time,
                    end = rangeDate.second.toDate().time
                )
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(context.getString(R.string.error_weekly_stats_room)))
                null
            }

            val moodList = localList?.takeIf { it.isNotEmpty() }?.map {
                Journal(
                    mood = it.mood,
                    moodLevel = it.moodLevel,
                    triggers = it.triggers,
                    tags = it.tags,
                    note = it.note,
                    createdAt = Timestamp(
                        it.createdAt / 1000,
                        ((it.createdAt % 1000) * 1000000).toInt()
                    )
                )
            } ?: run {
                try {
                    val snapshot = firestore.collection(FirestoreCollections.USERS)
                        .document(userId)
                        .collection(FirestoreCollections.SubCollections.JOURNAL_ENTRIES)
                        .whereGreaterThanOrEqualTo("createdAt", rangeDate.first)
                        .whereLessThanOrEqualTo("createdAt", rangeDate.second)
                        .get()
                        .await()
                    snapshot.documents.mapNotNull { it.toObject(Journal::class.java) }
                } catch (e: Exception) {
                    e.printStackTrace()
                    when (e) {
                        is FirebaseFirestoreException -> emit(Resource.Error(context.getString(R.string.error_weekly_stats_firestore)))
                        is FirebaseNetworkException -> emit(Resource.Error(context.getString(R.string.error_network)))
                        else -> emit(Resource.Error(context.getString(R.string.error_weekly_stats_firestore)))
                    }
                    return@flow
                }
            }

            if (moodList.isEmpty()) {
                emit(Resource.Success(null))
                return@flow
            }

            val moodCounts = moodList.groupingBy { it.mood }.eachCount()
            val total = moodList.size.toFloat()
            val moodDistribution = moodCounts
                .mapValues { it.value / total }
                .toList()
                .sortedByDescending { it.second }
                .toMap(LinkedHashMap())
            val dominantMood = moodCounts.maxByOrNull { it.value }?.key ?: "-"
            val activeDays = moodList.map {
                val cal = Calendar.getInstance()
                cal.time = it.createdAt.toDate()
                "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH)}-${cal.get(Calendar.DAY_OF_MONTH)}"
            }.distinct().count()

            emit(Resource.Success(
                WeeklyStats(
                    dominantMood = dominantMood,
                    activeDays = activeDays,
                    moodDistribution = moodDistribution
                )
            ))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(context.getString(R.string.error_weekly_stats_not_found)))
        }
    }

    override fun getDailyQuote(): Flow<Resource<Quote>> = flow {
        emit(Resource.Loading())
        try {
            val storedQuote = moodDataStore.dailyQuote.first()
            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val storedQuoteDate = storedQuote.createdAt?.let {
                Calendar.getInstance().apply {
                    timeInMillis = it
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
            }

            if (storedQuote.quoteText.isNotEmpty() && storedQuoteDate == today) {
                emit(Resource.Success(storedQuote))
                return@flow
            }

            val networkResource = object : NetworkOnlyResource<Quote, ZenQuotesResponse>() {
                override suspend fun createCall(): Flow<ApiResource<ZenQuotesResponse>> {
                    return remoteDataSource.getDailyQuote()
                }

                override fun loadFromNetwork(data: ZenQuotesResponse): Flow<Quote> = flow {
                    val newQuote = Quote(
                        quoteText = data.quote,
                        source = data.author,
                        createdAt = System.currentTimeMillis()
                    )

                    moodDataStore.setDailyQuote(newQuote)
                    val savedQuote = moodDataStore.dailyQuote.first()
                    emit(savedQuote)
                }
            }.asFlow()

            networkResource.collect { emit(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(context.getString(R.string.no_quote_description)))
        }
    }

    override fun getAverageMoodInWeek(userId: String): Flow<Resource<Double?>> = flow {
        emit(Resource.Loading())
        try {
            val currentWeek = getCurrentWeekRange()
            val startTimestamp = Timestamp(currentWeek.first)
            val endTimestamp = Timestamp(currentWeek.second)

            val snapshot = firestore.collection(FirestoreCollections.USERS)
                .document(userId)
                .collection(FirestoreCollections.SubCollections.JOURNAL_ENTRIES)
                .whereGreaterThanOrEqualTo("createdAt", startTimestamp)
                .whereLessThanOrEqualTo("createdAt", endTimestamp)
                .get()
                .await()

            val moodLevels = snapshot.documents.mapNotNull { it.getLong("mood_level") }
            if (moodLevels.isNotEmpty()) {
                val average = moodLevels.average()
                moodDataStore.setAverageMoodLevel(average)
                emit(Resource.Success(average))
            } else {
                emit(Resource.Success(null))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            try {
                val localAverage = moodDataStore.averageMoodLevel.first()
                if (localAverage > 0.0) {
                    emit(Resource.Success(localAverage))
                } else {
                    emit(Resource.Success(null))
                }
            } catch (e2: Exception) {
                e2.printStackTrace()
                emit(Resource.Error(context.getString(R.string.no_average_mood_not_found)))
            }
        }
    }

    override fun getMostFrequentMoodInWeek(userId: String): Flow<Resource<MostFrequentMood?>> = flow {
        emit(Resource.Loading())
        try {
            val currentWeek = getCurrentWeekRange()
            val startTimestamp = Timestamp(currentWeek.first)
            val endTimestamp = Timestamp(currentWeek.second)

            val snapshot = firestore.collection(FirestoreCollections.USERS)
                .document(userId)
                .collection(FirestoreCollections.SubCollections.JOURNAL_ENTRIES)
                .whereGreaterThanOrEqualTo("createdAt", startTimestamp)
                .whereLessThanOrEqualTo("createdAt", endTimestamp)
                .get()
                .await()

            val moods = snapshot.documents.mapNotNull { it.getString("mood") }
            if (moods.isNotEmpty()) {
                val moodCounts = moods.groupingBy { it }.eachCount()
                val mostFrequent = moodCounts.maxByOrNull { it.value }
                val mostFrequentMood = mostFrequent?.key
                val days = mostFrequent?.value
                if (mostFrequentMood != null && days != null) {
                    val result = MostFrequentMood(mood = mostFrequentMood, days = days)
                    moodDataStore.setMostFrequentMood(result)
                    emit(Resource.Success(result))
                } else {
                    emit(Resource.Success(null))
                }
            } else {
                emit(Resource.Success(null))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            try {
                val local = moodDataStore.mostFrequentMood.first()
                if (local.mood.isNotEmpty() && local.days > 0) {
                    emit(Resource.Success(local))
                } else {
                    emit(Resource.Success(null))
                }
            } catch (e2: Exception) {
                e2.printStackTrace()
                emit(Resource.Error(context.getString(R.string.no_most_frequent_mood)))
            }
        }
    }

    override fun getMoodTrendInWeek(
        userId: String,
        week: WeekData
    ): Flow<Resource<List<MoodTrendData>>> = flow {
        emit(Resource.Loading())
        try {
            val startTime = week.startDate.time
            val endTime = week.endDate.time

            val localData = try {
                moodHistoryDao.getMoodHistoryInRange(startTime, endTime)
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(context.getString(R.string.error_mood_trend)))
                null
            }

            if (!localData.isNullOrEmpty()) {
                val moodTrendList = localData.map { entity ->
                    MoodTrendData(
                        date = Date(entity.createdAt),
                        moodLevel = entity.moodLevel,
                        mood = entity.mood
                    )
                }
                emit(Resource.Success(moodTrendList))
                return@flow
            }

            try {
                val startTimestamp = Timestamp(startTime / 1000, ((startTime % 1000) * 1000000).toInt())
                val endTimestamp = Timestamp(endTime / 1000, ((endTime % 1000) * 1000000).toInt())

                val snapshot = firestore.collection(FirestoreCollections.USERS)
                    .document(userId)
                    .collection(FirestoreCollections.SubCollections.JOURNAL_ENTRIES)
                    .whereGreaterThanOrEqualTo("createdAt", startTimestamp)
                    .whereLessThanOrEqualTo("createdAt", endTimestamp)
                    .get()
                    .await()

                val entities = snapshot.documents.mapNotNull { doc ->
                    val journal = doc.toObject(Journal::class.java)
                    if (journal != null) {
                        MoodHistoryEntity(
                            documentId = doc.id,
                            mood = journal.mood,
                            moodLevel = journal.moodLevel,
                            triggers = journal.triggers,
                            tags = journal.tags,
                            note = journal.note,
                            createdAt = journal.createdAt.toDate().time
                        )
                    } else null
                }

                if (entities.isNotEmpty()) {
                    moodHistoryDao.insertAllMoodHistory(entities)
                }

                val moodTrendList = entities.map { entity ->
                    MoodTrendData(
                        date = Date(entity.createdAt),
                        moodLevel = entity.moodLevel,
                        mood = entity.mood
                    )
                }
                emit(Resource.Success(moodTrendList))
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is FirebaseFirestoreException -> emit(Resource.Error(context.getString(R.string.error_mood_trend)))
                    is FirebaseNetworkException -> emit(Resource.Error(context.getString(R.string.error_network)))
                    else -> emit(Resource.Error(context.getString(R.string.error_mood_trend_default)))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(context.getString(R.string.error_mood_trend_default)))
        }
    }

    override fun getEntryStreakInWeek(userId: String): Flow<Resource<Int>> = flow {
        emit(Resource.Loading())
        try {
            val currentWeek = getCurrentWeekRange()
            val startTimestamp = Timestamp(currentWeek.first)
            val endTimestamp = Timestamp(currentWeek.second)

            try {
                val snapshot = firestore.collection(FirestoreCollections.USERS)
                    .document(userId)
                    .collection(FirestoreCollections.SubCollections.JOURNAL_ENTRIES)
                    .whereGreaterThanOrEqualTo("createdAt", startTimestamp)
                    .whereLessThanOrEqualTo("createdAt", endTimestamp)
                    .get()
                    .await()

                val uniqueDays = snapshot.documents
                    .mapNotNull { it.getTimestamp("createdAt")?.toDate() }
                    .map { date ->
                        val cal = Calendar.getInstance()
                        cal.time = date
                        "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH)}-${cal.get(Calendar.DAY_OF_MONTH)}"
                    }
                    .distinct()
                    .count()

                moodDataStore.setEntryStreakDays(uniqueDays)
                emit(Resource.Success(uniqueDays))
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is FirebaseFirestoreException -> emit(Resource.Error(context.getString(R.string.error_entry_streak_default)))
                    is FirebaseNetworkException -> emit(Resource.Error(context.getString(R.string.error_network)))
                    else -> {
                        try {
                            val localStreak = moodDataStore.entryStreakDays.first()
                            emit(Resource.Success(localStreak))
                        } catch (e2: Exception) {
                            e2.printStackTrace()
                            emit(Resource.Error(context.getString(R.string.no_entry_streak)))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(context.getString(R.string.error_entry_streak_default)))
        }
    }

    override fun getTopTriggersInWeek(userId: String): Flow<Resource<List<String>>> = flow {
        emit(Resource.Loading())
        try {
            val currentWeek = getCurrentWeekRange()
            val startTimestamp = Timestamp(currentWeek.first)
            val endTimestamp = Timestamp(currentWeek.second)

            try {
                val snapshot = firestore.collection(FirestoreCollections.USERS)
                    .document(userId)
                    .collection(FirestoreCollections.SubCollections.JOURNAL_ENTRIES)
                    .whereGreaterThanOrEqualTo("createdAt", startTimestamp)
                    .whereLessThanOrEqualTo("createdAt", endTimestamp)
                    .get()
                    .await()

                val allTriggers = mutableListOf<String>()
                snapshot.documents.forEach { doc ->
                    val triggers = doc.get("triggers") as? List<*>
                    triggers?.forEach { trigger ->
                        if (trigger is String) {
                            allTriggers.add(trigger)
                        }
                    }
                }

                if (allTriggers.isNotEmpty()) {
                    val triggerCounts = allTriggers.groupingBy { it }.eachCount()

                    val topTriggers = triggerCounts.entries
                        .sortedByDescending { it.value }
                        .take(3)
                        .map { it.key }

                    moodDataStore.setTopTriggers(topTriggers)
                    emit(Resource.Success(topTriggers))
                } else {
                    emit(Resource.Success(emptyList()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                try {
                    val localTriggers = moodDataStore.topTriggers.first()
                    if (localTriggers.isNotEmpty()) {
                        emit(Resource.Success(localTriggers))
                    } else {
                        when (e) {
                            is FirebaseFirestoreException -> emit(Resource.Error(context.getString(R.string.error_top_triggers_default)))
                            is FirebaseNetworkException -> emit(Resource.Error(context.getString(R.string.error_network)))
                            else -> emit(Resource.Error(context.getString(R.string.error_top_triggers_not_found)))
                        }
                    }
                } catch (e2: Exception) {
                    e2.printStackTrace()
                    when (e) {
                        is FirebaseFirestoreException -> emit(Resource.Error(context.getString(R.string.error_top_triggers_default)))
                        is FirebaseNetworkException -> emit(Resource.Error(context.getString(R.string.error_network)))
                        else -> emit(Resource.Error(context.getString(R.string.error_top_triggers_not_found)))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(context.getString(R.string.error_top_triggers_default)))
        }
    }
}