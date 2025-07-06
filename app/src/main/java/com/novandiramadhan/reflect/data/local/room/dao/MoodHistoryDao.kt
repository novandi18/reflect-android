package com.novandiramadhan.reflect.data.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.novandiramadhan.reflect.data.local.room.entity.MoodHistoryEntity
import com.novandiramadhan.reflect.util.RoomConstrains.RoomTables
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMoodHistory(entities: List<MoodHistoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoodHistory(entity: MoodHistoryEntity)

    @Query("""
    SELECT * FROM ${RoomTables.MOOD_HISTORY}
    WHERE (:moodsSize = 0 OR mood IN (:selectedMoods))
    ORDER BY 
        CASE WHEN :sortDesc = 1 THEN createdAt END DESC,
        CASE WHEN :sortDesc = 0 THEN createdAt END ASC
    """)
    fun getFilteredMoodHistory(
        selectedMoods: List<String>,
        moodsSize: Int,
        sortDesc: Boolean
    ): PagingSource<Int, MoodHistoryEntity>

    @Query("SELECT * FROM ${RoomTables.MOOD_HISTORY} WHERE documentId = :documentId LIMIT 1")
    fun getMoodHistoryByDocumentId(documentId: String): Flow<MoodHistoryEntity?>

    @Query("SELECT * FROM ${RoomTables.MOOD_HISTORY} ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLastMoodHistory(): MoodHistoryEntity?

    @Query("SELECT * FROM ${RoomTables.MOOD_HISTORY} WHERE createdAt BETWEEN :start AND :end")
    suspend fun getMoodHistoryInRange(start: Long, end: Long): List<MoodHistoryEntity>

    @Delete
    suspend fun deleteMoodHistory(entity: MoodHistoryEntity)

    @Query("DELETE FROM ${RoomTables.MOOD_HISTORY}")
    suspend fun deleteAllMoodHistory()
}