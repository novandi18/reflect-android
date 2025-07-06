package com.novandiramadhan.reflect.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.novandiramadhan.reflect.data.local.room.converter.WeeklySummaryConverter
import com.novandiramadhan.reflect.domain.model.MoodTrendData
import com.novandiramadhan.reflect.domain.model.MostFrequentMood
import com.novandiramadhan.reflect.util.RoomConstrains.RoomTables

@Entity(tableName = RoomTables.WEEKLY_SUMMARY)
@TypeConverters(WeeklySummaryConverter::class)
data class WeeklySummaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val averageMood: Double? = null,
    val mostFrequentMood: MostFrequentMood? = null,
    val moodTrendData: List<MoodTrendData> = emptyList(),
    val entryStreak: Int = 0,
    val topTriggers: List<String> = emptyList(),
    val dominantMood: String,
    val activeDays: Int,
    val moodDistribution: Map<String, Float>
)
