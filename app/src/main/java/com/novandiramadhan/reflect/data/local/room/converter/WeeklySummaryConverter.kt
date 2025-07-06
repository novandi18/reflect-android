package com.novandiramadhan.reflect.data.local.room.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.novandiramadhan.reflect.domain.model.MoodTrendData
import com.novandiramadhan.reflect.domain.model.MostFrequentMood
import com.novandiramadhan.reflect.domain.model.WeeklyStats

class WeeklySummaryConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromMostFrequentMood(value: MostFrequentMood?): String? = gson.toJson(value)

    @TypeConverter
    fun toMostFrequentMood(value: String?): MostFrequentMood? =
        value?.let { gson.fromJson(it, MostFrequentMood::class.java) }

    @TypeConverter
    fun fromMoodTrendDataList(list: List<MoodTrendData>?): String? = gson.toJson(list)

    @TypeConverter
    fun toMoodTrendDataList(data: String?): List<MoodTrendData>? =
        data?.let {
            val type = object : TypeToken<List<MoodTrendData>>() {}.type
            gson.fromJson(it, type)
        }

    @TypeConverter
    fun fromWeeklyStats(value: WeeklyStats?): String? = gson.toJson(value)

    @TypeConverter
    fun toWeeklyStats(value: String?): WeeklyStats? =
        value?.let { gson.fromJson(it, WeeklyStats::class.java) }
}