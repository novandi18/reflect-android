package com.novandiramadhan.reflect.domain.repository

import androidx.paging.PagingData
import com.novandiramadhan.reflect.data.local.room.entity.MoodHistoryEntity
import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.util.state.MoodHistoryFilterState
import kotlinx.coroutines.flow.Flow

interface MoodHistoryRepository {
    fun getAllMoodHistory(
        userId: String,
        pageSize: Int = 10,
        filter: MoodHistoryFilterState
    ): Flow<PagingData<MoodHistoryEntity>>

    fun deleteMoodHistory(entity: MoodHistoryEntity): Flow<Resource<String>>
}