package com.novandiramadhan.reflect.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.novandiramadhan.reflect.domain.model.Notification
import com.novandiramadhan.reflect.notification.manager.WeeklySummaryNotificationManager
import com.novandiramadhan.reflect.util.ParcelizeKey
import com.novandiramadhan.reflect.util.parcelable
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WeeklySummaryReceiver : BroadcastReceiver() {
    @Inject
    lateinit var notificationManager: WeeklySummaryNotificationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        val notification = intent?.parcelable<Notification>(ParcelizeKey.NOTIFICATION)
        notification?.let {
            notificationManager.showNotification(it)
        }
    }
}