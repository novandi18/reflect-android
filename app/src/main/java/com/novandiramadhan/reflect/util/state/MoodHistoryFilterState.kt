package com.novandiramadhan.reflect.util.state

data class MoodHistoryFilterState(
    val selectedMoods: List<String> = emptyList(),
    val sortByDateDescending: Boolean = true
)