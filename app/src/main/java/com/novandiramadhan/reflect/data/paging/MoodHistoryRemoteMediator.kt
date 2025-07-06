package com.novandiramadhan.reflect.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.novandiramadhan.reflect.data.local.room.dao.MoodHistoryDao
import com.novandiramadhan.reflect.data.local.room.entity.MoodHistoryEntity
import com.novandiramadhan.reflect.domain.model.Journal
import com.novandiramadhan.reflect.util.FirestoreCollections
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalPagingApi::class)
class MoodHistoryRemoteMediator(
    private val userId: String,
    private val firestore: FirebaseFirestore,
    private val moodHistoryDao: MoodHistoryDao
): RemoteMediator<Int, MoodHistoryEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MoodHistoryEntity>
    ): MediatorResult {
        return try {
            val pageSize = state.config.pageSize
            val lastItem = state.lastItemOrNull()
            val query = firestore.collection(FirestoreCollections.USERS)
                .document(userId)
                .collection(FirestoreCollections.SubCollections.JOURNAL_ENTRIES)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .let {
                    if (loadType == LoadType.REFRESH) it.limit(pageSize.toLong())
                    else if (lastItem != null) it.startAfter(lastItem.createdAt).limit(pageSize.toLong())
                    else it.limit(pageSize.toLong())
                }

            val snapshot = query.get().await()
            val entities = snapshot.documents.mapNotNull { doc ->
                val data = doc.toObject(Journal::class.java)
                data?.let {
                    MoodHistoryEntity(
                        documentId = doc.id,
                        mood = it.mood,
                        moodLevel = it.moodLevel,
                        triggers = it.triggers,
                        tags = it.tags,
                        note = it.note,
                        createdAt = it.createdAt.toDate().time
                    )
                }
            }

            if (loadType == LoadType.REFRESH) {
                moodHistoryDao.deleteAllMoodHistory()
            }
            moodHistoryDao.insertAllMoodHistory(entities)
            MediatorResult.Success(endOfPaginationReached = entities.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}