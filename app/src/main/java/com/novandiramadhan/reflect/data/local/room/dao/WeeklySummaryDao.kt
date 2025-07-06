package com.novandiramadhan.reflect.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.novandiramadhan.reflect.data.local.room.entity.WeeklySummaryEntity
import com.novandiramadhan.reflect.util.RoomConstrains.RoomTables
import kotlinx.coroutines.flow.Flow

@Dao
interface WeeklySummaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeeklySummary(weeklySummary: WeeklySummaryEntity)

    @Query("SELECT * FROM ${RoomTables.WEEKLY_SUMMARY} WHERE id = :id")
    fun getWeeklySummaryById(id: Int): Flow<WeeklySummaryEntity?>

    @Query("DELETE FROM ${RoomTables.WEEKLY_SUMMARY} WHERE id = :id")
    suspend fun deleteWeeklySummary(id: Int)

    @Query("DELETE FROM ${RoomTables.WEEKLY_SUMMARY}")
    suspend fun deleteAllMonthlySummaries()
}