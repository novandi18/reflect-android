package com.novandiramadhan.reflect.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.novandiramadhan.reflect.data.local.room.dao.NotificationDao
import com.novandiramadhan.reflect.data.local.room.entity.NotificationEntity
import com.novandiramadhan.reflect.domain.model.NotificationType
import com.novandiramadhan.reflect.util.FirestoreCollections
import kotlinx.coroutines.tasks.await
import java.util.Date

@OptIn(ExperimentalPagingApi::class)
class NotificationRemoteMediator(
    private val userId: String,
    private val firestore: FirebaseFirestore,
    private val notificationDao: NotificationDao
): RemoteMediator<Int, NotificationEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NotificationEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    lastItem?.let {
                        Timestamp(Date(it.createdAt))
                    }
                }
            }

            val notificationsCollection = firestore.collection(FirestoreCollections.USERS)
                .document(userId)
                .collection(FirestoreCollections.SubCollections.NOTIFICATIONS)

            val query = if (loadKey == null) {
                notificationsCollection
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .limit(state.config.pageSize.toLong())
            } else {
                notificationsCollection
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .startAfter(loadKey)
                    .limit(state.config.pageSize.toLong())
            }


            val documents = query.get().await()
            val notifications = documents.mapNotNull { doc ->
                try {
                    NotificationEntity(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        message = doc.getString("message") ?: "",
                        type = doc.getString("type") ?: NotificationType.GENERAL.value,
                        isRead = doc.getBoolean("isRead") == true,
                        createdAt = doc.getTimestamp("createdAt")?.toDate()?.time
                            ?: System.currentTimeMillis()
                    )
                } catch (e: Exception) {
                    null
                }
            }

            if (loadType == LoadType.REFRESH) notificationDao.deleteAllNotifications()
            notificationDao.insertNotifications(notifications)

            MediatorResult.Success(
                endOfPaginationReached = notifications.isEmpty()
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}