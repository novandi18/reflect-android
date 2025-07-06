package com.novandiramadhan.reflect.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.novandiramadhan.reflect.data.local.room.entity.MonthlySummaryEntity
import com.novandiramadhan.reflect.util.RoomConstrains.RoomTables
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthlySummaryDao {
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertMonthlySummary(monthlySummary: MonthlySummaryEntity)

     @Query("SELECT * FROM ${RoomTables.MONTHLY_SUMMARY} WHERE id = :id")
     fun getMonthlySummaryById(id: Int): Flow<MonthlySummaryEntity?>

     @Query("DELETE FROM ${RoomTables.MONTHLY_SUMMARY} WHERE id = :id")
     suspend fun deleteMonthlySummary(id: Int)

     @Query("DELETE FROM ${RoomTables.MONTHLY_SUMMARY}")
     suspend fun deleteAllMonthlySummaries()
}