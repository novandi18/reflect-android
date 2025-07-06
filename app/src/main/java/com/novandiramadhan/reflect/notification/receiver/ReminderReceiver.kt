package com.novandiramadhan.reflect.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.novandiramadhan.reflect.data.local.room.dao.MoodHistoryDao
import com.novandiramadhan.reflect.domain.datastore.UserDataStore
import com.novandiramadhan.reflect.notification.manager.ReminderNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {
    @Inject
    lateinit var notificationManager: ReminderNotificationManager

    @Inject
    lateinit var moodHistoryDao: MoodHistoryDao

    @Inject
    lateinit var userDataStore: UserDataStore

    override fun onReceive(context: Context?, intent: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            val userData = userDataStore.state.first()
            val userId = userData.id
            if (userId.isEmpty()) return@launch

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startOfDay = calendar.timeInMillis

            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            val endOfDay = calendar.timeInMillis

            val todayEntries = moodHistoryDao.getMoodHistoryInRange(startOfDay, endOfDay)
            if (todayEntries.isEmpty()) {
                notificationManager.showNotification(userId)
            }
        }
    }
}