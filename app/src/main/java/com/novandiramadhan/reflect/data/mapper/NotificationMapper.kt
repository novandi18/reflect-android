package com.novandiramadhan.reflect.data.mapper

import com.google.firebase.Timestamp
import com.novandiramadhan.reflect.data.local.room.entity.NotificationEntity
import com.novandiramadhan.reflect.domain.model.Notification

object NotificationMapper {
    fun entityToDomain(entity: NotificationEntity): Notification {
        return Notification(
            id = entity.id,
            title = entity.title,
            message = entity.message,
            type = entity.type,
            isRead = entity.isRead,
            timestamp = Timestamp(entity.createdAt / 1000, ((entity.createdAt % 1000) * 1000000).toInt())
        )
    }

    fun domainToEntity(domain: Notification): NotificationEntity {
        return NotificationEntity(
            id = domain.id,
            title = domain.title,
            message = domain.message,
            type = domain.type,
            isRead = domain.isRead,
            createdAt = domain.timestamp.seconds * 1000 + domain.timestamp.nanoseconds / 1000000
        )
    }
}