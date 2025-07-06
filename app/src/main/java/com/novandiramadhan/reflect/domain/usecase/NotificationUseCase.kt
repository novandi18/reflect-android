package com.novandiramadhan.reflect.domain.usecase

import androidx.paging.PagingData
import com.novandiramadhan.reflect.data.local.room.entity.NotificationEntity
import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationUseCase {
    fun getAllNotifications(userId: String): Flow<PagingData<NotificationEntity>>
    fun insertNotification(userId: String, notification: Notification): Flow<Unit>
    fun deleteNotification(userId: String, notificationId: String): Flow<Resource<String>>
    fun deleteAllNotifications(userId: String): Flow<Resource<String>>
    fun markAsRead(userId: String, notificationId: String): Flow<Resource<String>>
    fun markAllAsRead(userId: String): Flow<Resource<String>>
}