package com.novandiramadhan.reflect.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.novandiramadhan.reflect.domain.datastore.MoodDataStore
import com.novandiramadhan.reflect.domain.model.MostFrequentMood
import com.novandiramadhan.reflect.domain.model.Quote
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoodDataStoreImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
): MoodDataStore {
    override val dailyQuote: Flow<Quote> = context.moodDataStore.data
        .catch { exception ->
            if (exception is Exception) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val quoteText = preferences[QUOTE_TEXT] ?: ""
            val source = preferences[SOURCE]
            val createdAt = preferences[CREATED_AT]

            Quote(
                quoteText = quoteText,
                source = source,
                createdAt = createdAt
            )
        }

    override val averageMoodLevel: Flow<Double> = context.moodDataStore.data
        .catch { exception ->
            if (exception is Exception) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[AVERAGE_MOOD_LEVEL] ?: 0.0
        }

    override val mostFrequentMood: Flow<MostFrequentMood> = context.moodDataStore.data
        .catch { exception ->
            if (exception is Exception) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val mostFrequentMood = preferences[MOST_FREQUENT_MOOD] ?: ""
            val mostFrequentMoodDays = preferences[MOST_FREQUENT_MOOD_DAYS] ?: 0

            MostFrequentMood(
                mood = mostFrequentMood,
                days = mostFrequentMoodDays
            )
        }

    override val entryStreakDays: Flow<Int> = context.moodDataStore.data
        .catch { exception ->
            if (exception is Exception) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[ENTRY_STREAK_DAYS] ?: 0
        }

    override val topTriggers: Flow<List<String>> = context.moodDataStore.data
        .catch { exception ->
            if (exception is Exception) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val triggersString = preferences[TOP_TRIGGERS] ?: ""
            if (triggersString.isEmpty()) emptyList() else triggersString.split(",")
        }

    override suspend fun setDailyQuote(quote: Quote) {
        context.moodDataStore.edit { preferences ->
            preferences[QUOTE_TEXT] = quote.quoteText
            preferences[SOURCE] = quote.source ?: ""
            preferences[CREATED_AT] = quote.createdAt ?: System.currentTimeMillis()
        }
    }

    override suspend fun setAverageMoodLevel(moodLevel: Double) {
        context.moodDataStore.edit { preferences ->
            preferences[AVERAGE_MOOD_LEVEL] = moodLevel
        }
    }

    override suspend fun setMostFrequentMood(mostFrequentMood: MostFrequentMood) {
        context.moodDataStore.edit { preferences ->
            preferences[MOST_FREQUENT_MOOD] = mostFrequentMood.mood
            preferences[MOST_FREQUENT_MOOD_DAYS] = mostFrequentMood.days
        }
    }

    override suspend fun setEntryStreakDays(days: Int) {
        context.moodDataStore.edit { preferences ->
            preferences[ENTRY_STREAK_DAYS] = days
        }
    }

    override suspend fun setTopTriggers(triggers: List<String>) {
        context.moodDataStore.edit { preferences ->
            preferences[TOP_TRIGGERS] = triggers.joinToString(",")
        }
    }

    override suspend fun deleteDailyQuote() {
        context.moodDataStore.edit { preferences ->
            preferences.remove(QUOTE_TEXT)
            preferences.remove(SOURCE)
            preferences.remove(CREATED_AT)
            preferences.clear()
        }
    }

    override suspend fun deleteAverageMoodLevel() {
        context.moodDataStore.edit { preferences ->
            preferences.remove(AVERAGE_MOOD_LEVEL)
            preferences.clear()
        }
    }

    override suspend fun deleteMostFrequentMood() {
        context.moodDataStore.edit { preferences ->
            preferences.remove(MOST_FREQUENT_MOOD)
            preferences.remove(MOST_FREQUENT_MOOD_DAYS)
            preferences.clear()
        }
    }

    override suspend fun deleteEntryStreakDays() {
        context.moodDataStore.edit { preferences ->
            preferences.remove(ENTRY_STREAK_DAYS)
            preferences.clear()
        }
    }

    override suspend fun deleteTopTriggers() {
        context.moodDataStore.edit { preferences ->
            preferences.remove(TOP_TRIGGERS)
        }
    }

    companion object {
        private val Context.moodDataStore by preferencesDataStore(name = "mood_preference")
        private val QUOTE_TEXT = stringPreferencesKey(name = "quote_text")
        private val SOURCE = stringPreferencesKey(name = "source")
        private val CREATED_AT = longPreferencesKey(name = "created_at")
        private val AVERAGE_MOOD_LEVEL = doublePreferencesKey(name = "average_mood_level")
        private val MOST_FREQUENT_MOOD = stringPreferencesKey(name = "most_frequent_mood")
        private val MOST_FREQUENT_MOOD_DAYS = intPreferencesKey(name = "most_frequent_mood")
        private val ENTRY_STREAK_DAYS = intPreferencesKey(name = "entry_streak_days")
        private val TOP_TRIGGERS = stringPreferencesKey(name = "top_triggers")
    }
}