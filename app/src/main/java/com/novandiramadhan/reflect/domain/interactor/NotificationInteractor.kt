package com.novandiramadhan.reflect.domain.interactor

import androidx.paging.PagingData
import com.novandiramadhan.reflect.data.local.room.entity.NotificationEntity
import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.domain.model.Notification
import com.novandiramadhan.reflect.domain.repository.NotificationRepository
import com.novandiramadhan.reflect.domain.usecase.NotificationUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationInteractor @Inject constructor(
    private val notificationRepository: NotificationRepository
): NotificationUseCase {
    override fun getAllNotifications(userId: String): Flow<PagingData<NotificationEntity>> =
        notificationRepository.getAllNotifications(userId)

    override fun insertNotification(userId: String, notification: Notification): Flow<Unit> =
        notificationRepository.insertNotification(userId, notification)

    override fun deleteNotification(
        userId: String,
        notificationId: String
    ): Flow<Resource<String>> = notificationRepository.deleteNotification(userId, notificationId)

    override fun deleteAllNotifications(userId: String): Flow<Resource<String>> =
        notificationRepository.deleteAllNotifications(userId)

    override fun markAsRead(userId: String, notificationId: String): Flow<Resource<String>> =
        notificationRepository.markAsRead(userId, notificationId)

    override fun markAllAsRead(userId: String): Flow<Resource<String>> =
        notificationRepository.markAllAsRead(userId)
}