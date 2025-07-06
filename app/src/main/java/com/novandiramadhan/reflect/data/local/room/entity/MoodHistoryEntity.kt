package com.novandiramadhan.reflect.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.novandiramadhan.reflect.util.RoomConstrains.RoomTables

@Entity(tableName = RoomTables.MOOD_HISTORY)
data class MoodHistoryEntity(
    @PrimaryKey
    val documentId: String = "",
    val mood: String = "",
    val moodLevel: Int = 0,
    val triggers: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val note: String = "",
    val createdAt: Long = 0L
)
