package com.novandiramadhan.reflect.domain.repository

import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.domain.model.MonthlySummary
import kotlinx.coroutines.flow.Flow

interface SummaryRepository {
    fun insertMonthlySummary(
        userId: String, summary: MonthlySummary
    ): Flow<Resource<Unit>>

    fun getMonthlySummary(
        userId: String, id: String
    ): Flow<Resource<MonthlySummary>>
}