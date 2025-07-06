package com.novandiramadhan.reflect.domain.interactor

import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.domain.model.MonthlySummary
import com.novandiramadhan.reflect.domain.repository.SummaryRepository
import com.novandiramadhan.reflect.domain.usecase.SummaryUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SummaryInteractor @Inject constructor(
    private val summaryRepository: SummaryRepository
): SummaryUseCase {
    override fun insertMonthlySummary(userId: String, summary: MonthlySummary): Flow<Resource<Unit>> =
        summaryRepository.insertMonthlySummary(userId, summary)

    override fun getMonthlySummary(
        userId: String,
        id: String
    ): Flow<Resource<MonthlySummary>> =
        summaryRepository.getMonthlySummary(userId, id)
}