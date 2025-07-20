package com.novandiramadhan.reflect.presentation.main

import android.widget.Toast
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.domain.datastore.SettingDataStore
import com.novandiramadhan.reflect.domain.datastore.UserDataStore
import com.novandiramadhan.reflect.domain.datastore.WelcomeDataStore
import com.novandiramadhan.reflect.domain.model.Setting
import com.novandiramadhan.reflect.domain.model.User
import com.novandiramadhan.reflect.notification.scheduler.NotificationScheduler
import com.novandiramadhan.reflect.presentation.navigation.Destinations
import com.novandiramadhan.reflect.ui.theme.ReflectTheme
import com.novandiramadhan.reflect.util.NotificationPermissionUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var userDataStore: UserDataStore

    @Inject
    lateinit var welcomeDataStore: WelcomeDataStore

    @Inject
    lateinit var settingDataStore: SettingDataStore

    @Inject
    lateinit var notificationScheduler: NotificationScheduler

    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        notificationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (!isGranted) {
                Toast.makeText(
                    this, getString(R.string.notification_denied), Toast.LENGTH_LONG
                ).show()
            }
        }

        setContent {
            val userState = userDataStore.state.collectAsState(initial = User()).value
            val welcomeState = welcomeDataStore.state.collectAsState(initial = false).value
            val settingState = settingDataStore.state.collectAsState(initial = Setting()).value

            val isDarkTheme = settingState.theme == "dark"

            val startDestination = when {
                !welcomeState -> Destinations.Welcome
                userState.id.isEmpty() -> Destinations.SignIn
                else -> Destinations.Home
            }

            LaunchedEffect(userState.id) {
                if (userState.id.isNotEmpty()) {
                    enableWeeklyNotifications()
                    enableMonthlyNotifications()
                }
            }

            if (startDestination == Destinations.Home && !NotificationPermissionUtil.hasNotificationPermission(this)) {
                NotificationPermissionUtil.requestNotificationPermission(
                    this,
                    notificationPermissionLauncher
                )
            }

            ReflectTheme(
                darkTheme = isDarkTheme
            ) {
                ReflectApp(
                    startDestination = startDestination
                )
            }
        }
    }

    private fun enableWeeklyNotifications() {
        if (NotificationPermissionUtil.hasNotificationPermission(this)) {
            try {
                notificationScheduler.scheduleWeeklySummary()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun enableMonthlyNotifications() {
        if (NotificationPermissionUtil.hasNotificationPermission(this)) {
            try {
                notificationScheduler.scheduleMonthlySummary()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}