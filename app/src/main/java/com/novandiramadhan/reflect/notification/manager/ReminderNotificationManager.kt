package com.novandiramadhan.reflect.notification.manager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.domain.model.Notification
import com.novandiramadhan.reflect.domain.model.NotificationType
import com.novandiramadhan.reflect.domain.usecase.NotificationUseCase
import com.novandiramadhan.reflect.presentation.main.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationUseCase: NotificationUseCase
) {
    private val channelId = context.getString(R.string.reminder_channel)
    private val notificationId = 1
    private val requestCode = 1

    init {
        createChannel()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.reminder_channel_name)
            val descriptionText = context.getString(R.string.reminder_channel_desc)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(userId: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notificationDesc = context.resources.getStringArray(
            R.array.reminder_notification_descriptions
        ).random()

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.app_notifications)
            .setContentTitle(context.getString(R.string.reminder_notification_title))
            .setContentText(notificationDesc)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                context.getString(R.string.reminder_notification_title),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, builder.build())
        saveNotification(userId, notificationDesc)
    }

    private fun saveNotification(userId: String, content: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val notification = Notification(
                id = UUID.randomUUID().toString(),
                title = context.getString(R.string.reminder_notification_title),
                message = content,
                isRead = false,
                type = NotificationType.MOOD_REMINDER.value
            )

            notificationUseCase.insertNotification(userId, notification).collect {}
        }
    }
}