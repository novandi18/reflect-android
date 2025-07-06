package com.novandiramadhan.reflect.presentation.state

sealed class JournalEntryUiState {
    data object Idle : JournalEntryUiState()
    data object Loading : JournalEntryUiState()
    data class Success(val message: String) : JournalEntryUiState()
    data class Error(val message: String) : JournalEntryUiState()
}