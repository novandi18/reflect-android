package com.novandiramadhan.reflect.presentation.main

import android.widget.Toast
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
import com.novandiramadhan.reflect.util.biometric.BiometricAuthListener
import com.novandiramadhan.reflect.util.biometric.BiometricUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BiometricAuthListener {
    @Inject
    lateinit var userDataStore: UserDataStore

    @Inject
    lateinit var welcomeDataStore: WelcomeDataStore

    @Inject
    lateinit var settingDataStore: SettingDataStore

    @Inject
    lateinit var notificationScheduler: NotificationScheduler

    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>
    private var biometricPrompt: BiometricPrompt? = null
    private var isAuthenticated = false
    private var isAuthenticationFailed = false
    private var onAuthenticationSuccess: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        isAuthenticated = false
        isAuthenticationFailed = false

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
            var showContent by remember { mutableStateOf(false) }
            var showBlur by remember { mutableStateOf(false) }

            val startDestination = when {
                !welcomeState -> Destinations.Welcome
                userState.id.isEmpty() -> Destinations.SignIn
                else -> Destinations.Home
            }

            LaunchedEffect(isAuthenticated, isAuthenticationFailed) {
                showBlur = if (settingState.useBiometric &&
                    userState.id.isNotEmpty() &&
                    startDestination == Destinations.Home &&
                    BiometricUtils.isBiometricReady(this@MainActivity)) {
                    !isAuthenticated
                } else {
                    false
                }
            }

            LaunchedEffect(settingState.useBiometric, userState.id, startDestination) {
                if (settingState.useBiometric &&
                    userState.id.isNotEmpty() &&
                    startDestination == Destinations.Home &&
                    BiometricUtils.isBiometricReady(this@MainActivity) &&
                    !isAuthenticated) {
                    showBlur = true
                    showContent = true
                    onAuthenticationSuccess = {
                        showBlur = false
                    }
                    initBiometricAuthentication()
                } else {
                    showContent = true
                    showBlur = false
                }
            }


            if (startDestination == Destinations.Home && !NotificationPermissionUtil.hasNotificationPermission(this)) {
                NotificationPermissionUtil.requestNotificationPermission(
                    this,
                    notificationPermissionLauncher
                )
            }

            if (showContent) {
                Box(modifier = Modifier.fillMaxSize()) {
                    ReflectTheme(
                        darkTheme = isDarkTheme
                    ) {
                        ReflectApp(
                            modifier = if (showBlur) Modifier.blur(20.dp) else Modifier,
                            startDestination = startDestination
                        )
                    }

                    if (showBlur) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.3f))
                        )
                    }
                }
            }
        }
    }

    private fun initBiometricAuthentication() {
        if (BiometricUtils.isBiometricReady(this)) {
            biometricPrompt = BiometricUtils.initBiometricPrompt(this, this)

            val promptInfo = BiometricUtils.createPromptInfo(
                title = getString(R.string.biometric_title),
                description = getString(R.string.biometric_description),
                negativeText = getString(R.string.cancel)
            )

            biometricPrompt?.authenticate(promptInfo)
        }
    }


    override fun onBiometricAuthenticateError(error: Int, errMsg: String) {
        when (error) {
            BiometricPrompt.ERROR_USER_CANCELED,
            BiometricPrompt.ERROR_NEGATIVE_BUTTON -> {
                // User canceled authentication, close the app
                finish()
            }
            BiometricPrompt.ERROR_LOCKOUT,
            BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> {
                finish()
            }
            else -> {
                isAuthenticationFailed = true
                initBiometricAuthentication()
            }
        }
    }

    override fun onAuthenticationFailed() {
        isAuthenticationFailed = true
    }

    override fun onBiometricAuthenticateSuccess(result: BiometricPrompt.AuthenticationResult) {
        isAuthenticated = true
        isAuthenticationFailed = false
        onAuthenticationSuccess?.invoke()
    }

    override fun onResume() {
        super.onResume()
        isAuthenticated = false
        isAuthenticationFailed = false
    }
}