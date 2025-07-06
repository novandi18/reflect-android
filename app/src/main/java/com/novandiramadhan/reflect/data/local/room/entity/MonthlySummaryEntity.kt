package com.novandiramadhan.reflect.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.novandiramadhan.reflect.data.local.room.converter.MonthlySummaryConverter
import com.novandiramadhan.reflect.domain.model.MostFrequentMood
import com.novandiramadhan.reflect.domain.model.SummaryMoodTrend
import com.novandiramadhan.reflect.util.RoomConstrains.RoomTables

@Entity(tableName = RoomTables.MONTHLY_SUMMARY)
@TypeConverters(MonthlySummaryConverter::class)
data class MonthlySummaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val averageMood: Double? = null,
    val mostFrequentMood: MostFrequentMood? = null,
    val moodTrend: List<SummaryMoodTrend> = emptyList(),
    val entryStreak: Int = 0,
    val topTriggers: List<String> = emptyList(),
    val dominantMood: String,
    val activeDays: Int,
    val moodDistribution: Map<String, Float>
)
