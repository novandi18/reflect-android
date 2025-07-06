package com.novandiramadhan.reflect.data.local.room.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.novandiramadhan.reflect.domain.model.MostFrequentMood
import com.novandiramadhan.reflect.domain.model.SummaryMoodTrend
import com.novandiramadhan.reflect.domain.model.WeeklyStats

class MonthlySummaryConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromMostFrequentMood(value: MostFrequentMood?): String? = gson.toJson(value)

    @TypeConverter
    fun toMostFrequentMood(value: String?): MostFrequentMood? =
        value?.let { gson.fromJson(it, MostFrequentMood::class.java) }

    @TypeConverter
    fun fromMoodTrendList(list: List<SummaryMoodTrend>?): String? = gson.toJson(list)

    @TypeConverter
    fun toMoodTrendList(data: String?): List<SummaryMoodTrend>? =
        data?.let {
            val type = object : TypeToken<List<SummaryMoodTrend>>() {}.type
            gson.fromJson(it, type)
        }

    @TypeConverter
    fun fromWeeklyStats(value: WeeklyStats?): String? = gson.toJson(value)

    @TypeConverter
    fun toWeeklyStats(value: String?): WeeklyStats? =
        value?.let { gson.fromJson(it, WeeklyStats::class.java) }
}