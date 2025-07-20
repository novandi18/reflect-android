package com.novandiramadhan.reflect.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.firebase.Timestamp
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.domain.model.Notification
import com.novandiramadhan.reflect.domain.model.NotificationType
import com.novandiramadhan.reflect.domain.usecase.MoodUseCase
import com.novandiramadhan.reflect.domain.usecase.UserUseCase
import com.novandiramadhan.reflect.notification.manager.MonthlySummaryNotificationManager
import com.novandiramadhan.reflect.util.getCurrentMonthRange
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MonthlySummaryReceiver : BroadcastReceiver() {
    @Inject
    lateinit var notificationManager: MonthlySummaryNotificationManager

    @Inject
    lateinit var moodUseCase: MoodUseCase

    @Inject
    lateinit var userUseCase: UserUseCase

    override fun onReceive(context: Context?, intent: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val currentUser = userUseCase.getCurrentUser().first()
                if (currentUser is Resource.Success && currentUser.data != null) {
                    val monthRange = getCurrentMonthRange()
                    val monthlyStats = moodUseCase.getMonthlyStats(
                        currentUser.data.id,
                        Pair(Timestamp(monthRange.first), Timestamp(monthRange.second))
                    ).first()

                    val notification = when {
                        monthlyStats is Resource.Success && monthlyStats.data != null -> {
                            val stats = monthlyStats.data
                            Notification(
                                title = context?.getString(R.string.monthly_summary_notification_title)
                                    ?: "Monthly Summary",
                                message = "Dominant mood: ${stats.dominantMood}. Active days: ${stats.activeDays}",
                                type = NotificationType.INSIGHT_UPDATE.value,
                                timestamp = Timestamp.now()
                            )
                        }
                        else -> {
                            Notification(
                                title = context?.getString(R.string.monthly_summary_notification_title)
                                    ?: "Monthly Summary",
                                message = context?.getString(R.string.monthly_summary_notification_desc)
                                    ?: "Check your monthly mood summary!",
                                type = NotificationType.INSIGHT_UPDATE.value,
                                timestamp = Timestamp.now()
                            )
                        }
                    }
                    notificationManager.showNotification(notification)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}