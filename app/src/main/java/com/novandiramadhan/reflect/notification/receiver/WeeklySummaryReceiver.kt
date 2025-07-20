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
import com.novandiramadhan.reflect.domain.usecase.NotificationUseCase
import com.novandiramadhan.reflect.domain.usecase.UserUseCase
import com.novandiramadhan.reflect.notification.manager.WeeklySummaryNotificationManager
import com.novandiramadhan.reflect.util.getCurrentWeekRange
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WeeklySummaryReceiver : BroadcastReceiver() {
    @Inject
    lateinit var notificationManager: WeeklySummaryNotificationManager

    @Inject
    lateinit var moodRepository: MoodUseCase

    @Inject
    lateinit var userRepository: UserUseCase

    @Inject
    lateinit var notificationRepository: NotificationUseCase

    override fun onReceive(context: Context?, intent: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val currentUser = userRepository.getCurrentUser().first()
                if (currentUser is Resource.Success && currentUser.data != null) {
                    val weekRange = getCurrentWeekRange()
                    val weeklyStats = moodRepository.getWeeklyStats(
                        currentUser.data.id,
                        Pair(Timestamp(weekRange.first), Timestamp(weekRange.second))
                    ).first()

                    val notification = when {
                        weeklyStats is Resource.Success && weeklyStats.data != null -> {
                            val stats = weeklyStats.data
                            Notification(
                                id = "weekly_${System.currentTimeMillis()}",
                                title = context?.getString(R.string.weekly_summary_notification_title)
                                    ?: "Weekly Summary",
                                message = "Dominant mood: ${stats.dominantMood}. Active days: ${stats.activeDays}/7",
                                type = NotificationType.INSIGHT_UPDATE.value,
                                timestamp = Timestamp.now()
                            )
                        }
                        else -> {
                            Notification(
                                id = "weekly_${System.currentTimeMillis()}",
                                title = context?.getString(R.string.weekly_summary_notification_title)
                                    ?: "Weekly Summary",
                                message = context?.getString(R.string.weekly_summary_notification_desc)
                                    ?: "Your weekly summary is here!",
                                type = NotificationType.INSIGHT_UPDATE.value,
                                timestamp = Timestamp.now()
                            )
                        }
                    }

                    notificationRepository.insertNotification(currentUser.data.id, notification).collect{}

                    notificationManager.showNotification(notification)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}