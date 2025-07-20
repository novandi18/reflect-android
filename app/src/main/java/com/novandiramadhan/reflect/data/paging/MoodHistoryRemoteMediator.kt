package com.novandiramadhan.reflect.data.paging

import android.util.Log
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
            val loadSize = when (loadType) {
                LoadType.REFRESH -> state.config.initialLoadSize
                LoadType.APPEND  -> state.config.pageSize
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            }

            val lastItem = state.lastItemOrNull()

            val baseQuery = firestore.collection(FirestoreCollections.USERS)
                .document(userId)
                .collection(FirestoreCollections.SubCollections.JOURNAL_ENTRIES)
                .orderBy("createdAt", Query.Direction.DESCENDING)

            val pageQuery = when (loadType) {
                LoadType.REFRESH ->
                    baseQuery.limit(loadSize.toLong())
                LoadType.APPEND ->
                    if (lastItem != null)
                        baseQuery.startAfter(lastItem.createdAt).limit(loadSize.toLong())
                    else
                        baseQuery.limit(loadSize.toLong())
                else -> throw IllegalStateException("Unexpected loadType $loadType")
            }

            val snapshot = pageQuery.get().await()
            val entities = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Journal::class.java)?.let {
                    MoodHistoryEntity(
                        documentId = doc.id,
                        mood = it.mood,
                        moodLevel = doc.getLong("mood_level")?.toInt() ?: 1,
                        triggers = it.triggers,
                        tags = it.tags,
                        note = it.note,
                        createdAt = it.createdAt.toDate().time
                    )
                }
            }

            if (loadType == LoadType.REFRESH) {
                moodHistoryDao.deleteAllMoodHistory()
                moodHistoryDao.insertAllMoodHistory(entities)
            } else {
                moodHistoryDao.insertAllMoodHistory(entities)
            }

            MediatorResult.Success(endOfPaginationReached = entities.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
