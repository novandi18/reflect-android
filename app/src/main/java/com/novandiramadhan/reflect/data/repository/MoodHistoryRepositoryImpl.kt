package com.novandiramadhan.reflect.data.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FirebaseFirestore
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.data.local.room.dao.MoodHistoryDao
import com.novandiramadhan.reflect.data.local.room.entity.MoodHistoryEntity
import com.novandiramadhan.reflect.data.paging.MoodHistoryRemoteMediator
import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.domain.repository.MoodHistoryRepository
import com.novandiramadhan.reflect.util.FirestoreCollections
import com.novandiramadhan.reflect.util.state.MoodHistoryFilterState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MoodHistoryRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firestore: FirebaseFirestore,
    private val moodHistoryDao: MoodHistoryDao,
): MoodHistoryRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getAllMoodHistory(
        userId: String,
        pageSize: Int,
        filter: MoodHistoryFilterState
    ): Flow<PagingData<MoodHistoryEntity>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
            remoteMediator = MoodHistoryRemoteMediator(userId, firestore, moodHistoryDao),
            pagingSourceFactory = {
                moodHistoryDao.getFilteredMoodHistory(
                    selectedMoods = filter.selectedMoods,
                    moodsSize = filter.selectedMoods.size,
                    sortDesc = filter.sortByDateDescending
                )
            }
        ).flow
    }

    override fun deleteMoodHistory(entity: MoodHistoryEntity): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            firestore.collection(FirestoreCollections.USERS)
                .document(entity.documentId)
                .collection(FirestoreCollections.SubCollections.JOURNAL_ENTRIES)
                .document(entity.documentId)
                .delete()
                .await()

            moodHistoryDao.deleteMoodHistory(entity)
            emit(Resource.Success(context.getString(R.string.success_delete_mood_history)))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(context.getString(R.string.error_delete_mood_history)))
        }
    }
}