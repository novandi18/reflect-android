package com.novandiramadhan.reflect.presentation.state

import com.novandiramadhan.reflect.domain.model.Quote

sealed class DailyQuoteUiState {
    object Idle : DailyQuoteUiState()
    object Loading : DailyQuoteUiState()
    data class Success(val quote: Quote) : DailyQuoteUiState()
    data class Error(val message: String) : DailyQuoteUiState()
}