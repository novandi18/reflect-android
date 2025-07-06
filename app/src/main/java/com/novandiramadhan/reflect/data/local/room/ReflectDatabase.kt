package com.novandiramadhan.reflect.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.novandiramadhan.reflect.data.local.room.converter.MapConverter
import com.novandiramadhan.reflect.data.local.room.converter.StringListConverter
import com.novandiramadhan.reflect.data.local.room.dao.MonthlySummaryDao
import com.novandiramadhan.reflect.data.local.room.dao.MoodHistoryDao
import com.novandiramadhan.reflect.data.local.room.dao.NotificationDao
import com.novandiramadhan.reflect.data.local.room.dao.WeeklySummaryDao
import com.novandiramadhan.reflect.data.local.room.entity.MonthlySummaryEntity
import com.novandiramadhan.reflect.data.local.room.entity.MoodHistoryEntity
import com.novandiramadhan.reflect.data.local.room.entity.NotificationEntity
import com.novandiramadhan.reflect.data.local.room.entity.WeeklySummaryEntity

@Database(
    entities = [
        MoodHistoryEntity::class,
        NotificationEntity::class,
        MonthlySummaryEntity::class,
        WeeklySummaryEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    StringListConverter::class,
    MapConverter::class
)
abstract class ReflectDatabase: RoomDatabase() {
    abstract fun moodHistoryDao(): MoodHistoryDao
    abstract fun notificationDao(): NotificationDao
    abstract fun monthlySummaryDao(): MonthlySummaryDao
    abstract fun weeklySummaryDao(): WeeklySummaryDao
}