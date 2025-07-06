package com.novandiramadhan.reflect.domain.interactor

import androidx.paging.PagingData
import com.novandiramadhan.reflect.data.local.room.entity.MoodHistoryEntity
import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.domain.repository.MoodHistoryRepository
import com.novandiramadhan.reflect.domain.usecase.MoodHistoryUseCase
import com.novandiramadhan.reflect.util.state.MoodHistoryFilterState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoodHistoryInteractor @Inject constructor(
    private val moodHistoryRepository: MoodHistoryRepository,
): MoodHistoryUseCase {
    override fun getAllMoodHistory(
        userId: String, pageSize: Int, filter: MoodHistoryFilterState
    ): Flow<PagingData<MoodHistoryEntity>> =
        moodHistoryRepository.getAllMoodHistory(userId, pageSize, filter)

    override fun deleteMoodHistory(entity: MoodHistoryEntity): Flow<Resource<String>> =
        moodHistoryRepository.deleteMoodHistory(entity)
}