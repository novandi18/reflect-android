package com.novandiramadhan.reflect.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.novandiramadhan.reflect.domain.model.NotificationType
import com.novandiramadhan.reflect.util.RoomConstrains.RoomTables

@Entity(tableName = RoomTables.NOTIFICATION)
data class NotificationEntity(
    @PrimaryKey
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val type: String = NotificationType.GENERAL.value,
    val isRead: Boolean = false,
    val createdAt: Long = 0L
)
