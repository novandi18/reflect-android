package com.novandiramadhan.reflect.data.repository

import android.content.Context
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.novandiramadhan.reflect.data.local.room.dao.MonthlySummaryDao
import com.novandiramadhan.reflect.data.local.room.dao.WeeklySummaryDao
import com.novandiramadhan.reflect.data.local.room.entity.MonthlySummaryEntity
import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.domain.model.MonthlySummary
import com.novandiramadhan.reflect.domain.repository.SummaryRepository
import com.novandiramadhan.reflect.util.FirestoreCollections
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar
import javax.inject.Inject

class SummaryRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firestore: FirebaseFirestore,
    private val monthlySummaryDao: MonthlySummaryDao,
    private val weeklySummaryDao: WeeklySummaryDao,
): SummaryRepository {
    override fun insertMonthlySummary(
        userId: String,
        summary: MonthlySummary
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, -1)
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val documentId = "$year-$month"

            val monthlySummaryMap = hashMapOf(
                "averageMood" to summary.averageMood.toString(),
                "mostFrequentMood" to if (summary.mostFrequentMood != null) {
                    hashMapOf(
                        "mood" to summary.mostFrequentMood.mood,
                        "days" to summary.mostFrequentMood.days
                    )
                } else null,
                "moodTrend" to summary.moodTrend.map { trend ->
                    hashMapOf(
                        "week" to trend.week,
                        "averageMoodLevel" to trend.averageMoodLevel,
                        "averageMood" to trend.averageMood
                    )
                },
                "entryStreak" to summary.entryStreak,
                "topTriggers" to summary.topTriggers,
                "dominantMood" to summary.dominantMood,
                "activeDays" to summary.activeDays,
                "moodDistribution" to summary.moodDistribution
            )

            val documentRef = firestore
                .collection(FirestoreCollections.USERS)
                .document(userId)
                .collection(FirestoreCollections.SubCollections.MONTHLY_SUMMARIES)
                .document(documentId)

            Tasks.await(documentRef.set(monthlySummaryMap))

            val monthlySummaryEntity = MonthlySummaryEntity(
                id = documentId.hashCode(),
                averageMood = summary.averageMood,
                mostFrequentMood = summary.mostFrequentMood,
                moodTrend = summary.moodTrend,
                entryStreak = summary.entryStreak,
                topTriggers = summary.topTriggers,
                dominantMood = summary.dominantMood,
                activeDays = summary.activeDays,
                moodDistribution = summary.moodDistribution
            )
            monthlySummaryDao.insertMonthlySummary(monthlySummaryEntity)

            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }

    override fun getMonthlySummary(
        userId: String,
        id: String
    ): Flow<Resource<MonthlySummary>> = flow {
        emit(Resource.Loading())
        try {
            val idHash = id.hashCode()

            monthlySummaryDao.getMonthlySummaryById(idHash).collect { entity ->
                if (entity != null) {
                    val monthlySummary = MonthlySummary(
                        averageMood = entity.averageMood,
                        mostFrequentMood = entity.mostFrequentMood,
                        moodTrend = entity.moodTrend,
                        entryStreak = entity.entryStreak,
                        topTriggers = entity.topTriggers,
                        dominantMood = entity.dominantMood,
                        activeDays = entity.activeDays,
                        moodDistribution = entity.moodDistribution
                    )
                    emit(Resource.Success(monthlySummary))
                } else {
                    emit(Resource.Error("Monthly summary not found"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }
}