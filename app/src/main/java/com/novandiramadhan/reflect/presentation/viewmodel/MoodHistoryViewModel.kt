package com.novandiramadhan.reflect.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.novandiramadhan.reflect.data.local.room.entity.MoodHistoryEntity
import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.domain.datastore.UserDataStore
import com.novandiramadhan.reflect.domain.usecase.MoodHistoryUseCase
import com.novandiramadhan.reflect.util.state.MoodHistoryFilterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoodHistoryViewModel @Inject constructor(
    private val moodHistoryUseCase: MoodHistoryUseCase,
    userDataStore: UserDataStore
): ViewModel() {
    private val _deleteResult = MutableStateFlow<Resource<String>?>(null)
    val deleteResult: StateFlow<Resource<String>?> = _deleteResult.asStateFlow()

    private val _showFilterDialog = MutableStateFlow(false)
    val showFilterDialog: StateFlow<Boolean> = _showFilterDialog.asStateFlow()

    private val _deleteDialog = MutableStateFlow(Pair(false, null as MoodHistoryEntity?))
    val deleteDialog: StateFlow<Pair<Boolean, MoodHistoryEntity?>> = _deleteDialog.asStateFlow()

    val user = userDataStore.state
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _filterState = MutableStateFlow(MoodHistoryFilterState())
    val filterState: StateFlow<MoodHistoryFilterState> = _filterState

    fun setFilterState(filter: MoodHistoryFilterState) {
        _filterState.value = filter
    }

    fun setShowFilterDialog(show: Boolean) {
        _showFilterDialog.value = show
    }

    fun setDeleteDialog(show: Boolean, entity: MoodHistoryEntity?) {
        _deleteDialog.value = Pair(show, entity)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val moodHistory: Flow<PagingData<MoodHistoryEntity>> = combine(user, _filterState) { user, filter ->
        user?.let {
            moodHistoryUseCase.getAllMoodHistory(it.id, 10, filter)
        }
    }.filterNotNull().flatMapLatest { it }.cachedIn(viewModelScope)

    fun deleteMoodHistory(entity: MoodHistoryEntity) {
        viewModelScope.launch {
            moodHistoryUseCase.deleteMoodHistory(entity)
                .collect { _deleteResult.value = it }
        }
    }

    fun clearDeleteResult() {
        _deleteResult.value = null
    }
}