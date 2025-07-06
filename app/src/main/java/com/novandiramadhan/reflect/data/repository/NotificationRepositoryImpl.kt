package com.novandiramadhan.reflect.data.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FirebaseFirestore
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.data.local.room.dao.NotificationDao
import com.novandiramadhan.reflect.data.local.room.entity.NotificationEntity
import com.novandiramadhan.reflect.data.paging.NotificationRemoteMediator
import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.domain.model.Notification
import com.novandiramadhan.reflect.domain.repository.NotificationRepository
import com.novandiramadhan.reflect.util.FirestoreCollections
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firestore: FirebaseFirestore,
    private val notificationDao: NotificationDao
): NotificationRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getAllNotifications(userId: String): Flow<PagingData<NotificationEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 40
            ),
            remoteMediator = NotificationRemoteMediator(
                userId = userId,
                firestore = firestore,
                notificationDao = notificationDao
            ),
            pagingSourceFactory = {
                notificationDao.getNotifications()
            }
        ).flow
    }

    override fun insertNotification(
        userId: String,
        notification: Notification
    ): Flow<Unit> = flow {
        try {
            val docRef = firestore.collection(FirestoreCollections.USERS)
                .document(userId)
                .collection(FirestoreCollections.SubCollections.NOTIFICATIONS)
                .document(notification.id)

            val data = hashMapOf(
                "id" to notification.id,
                "title" to notification.title,
                "message" to notification.message,
                "type" to notification.type,
                "isRead" to notification.isRead,
                "createdAt" to notification.timestamp
            )

            docRef.set(data).await()

            val entity = NotificationEntity(
                id = notification.id,
                title = notification.title,
                message = notification.message,
                type = notification.type,
                isRead = notification.isRead,
                createdAt = notification.timestamp.toDate().time
            )

            notificationDao.insertNotification(entity)
            emit(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override fun deleteNotification(
        userId: String,
        notificationId: String
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val docRef = firestore.collection(FirestoreCollections.USERS)
                .document(userId)
                .collection(FirestoreCollections.SubCollections.NOTIFICATIONS)
                .document(notificationId)

            docRef.delete().await()
            notificationDao.deleteNotification(notificationId)
            emit(Resource.Success(notificationId))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(context.getString(R.string.error_delete_notification)))
        }
    }

    override fun deleteAllNotifications(userId: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val collectionRef = firestore.collection(FirestoreCollections.USERS)
                .document(userId)
                .collection(FirestoreCollections.SubCollections.NOTIFICATIONS)

            val snapshot = collectionRef.get().await()
            val batch = firestore.batch()
            for (doc in snapshot.documents) {
                batch.delete(doc.reference)
            }

            batch.commit().await()
            notificationDao.deleteAllNotifications()
            emit(Resource.Success(userId))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(context.getString(R.string.error_delete_notification)))
        }
    }

    override fun markAsRead(userId: String, notificationId: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val docRef = firestore.collection(FirestoreCollections.USERS)
                .document(userId)
                .collection(FirestoreCollections.SubCollections.NOTIFICATIONS)
                .document(notificationId)
            docRef.update("isRead", true).await()

            notificationDao.markAsRead(notificationId)
            emit(Resource.Success(notificationId))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(context.getString(R.string.error_mark_notification)))
        }
    }

    override fun markAllAsRead(userId: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val collectionRef = firestore.collection(FirestoreCollections.USERS)
                .document(userId)
                .collection(FirestoreCollections.SubCollections.NOTIFICATIONS)
            val snapshot = collectionRef.get().await()
            val batch = firestore.batch()
            for (doc in snapshot.documents) {
                batch.update(doc.reference, "isRead", true)
            }
            batch.commit().await()

            notificationDao.markAllAsRead()
            emit(Resource.Success(userId))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(context.getString(R.string.error_mark_all_notification)))
        }
    }
}