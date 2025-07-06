package com.novandiramadhan.reflect.data.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.novandiramadhan.reflect.data.local.room.entity.NotificationEntity
import com.novandiramadhan.reflect.util.RoomConstrains.RoomTables

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("SELECT * FROM ${RoomTables.NOTIFICATION} ORDER BY createdAt DESC")
    fun getNotifications(): PagingSource<Int, NotificationEntity>

    @Query("UPDATE ${RoomTables.NOTIFICATION} SET isRead = 1 WHERE id = :notificationId")
    suspend fun markAsRead(notificationId: String)

    @Query("UPDATE ${RoomTables.NOTIFICATION} SET isRead = 1")
    suspend fun markAllAsRead()

    @Query("DELETE FROM ${RoomTables.NOTIFICATION} WHERE id = :notificationId")
    suspend fun deleteNotification(notificationId: String)

    @Query("DELETE FROM ${RoomTables.NOTIFICATION}")
    suspend fun deleteAllNotifications()
}