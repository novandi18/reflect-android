package com.novandiramadhan.reflect.presentation.screen

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Support
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.presentation.component.ConfirmationDialog
import com.novandiramadhan.reflect.presentation.component.RDropdown
import com.novandiramadhan.reflect.presentation.component.TimePickerDialog
import com.novandiramadhan.reflect.presentation.component.profile.ClickableSettingItem
import com.novandiramadhan.reflect.presentation.component.profile.SettingItem
import com.novandiramadhan.reflect.presentation.state.SettingUiState
import com.novandiramadhan.reflect.presentation.viewmodel.ProfileViewModel
import com.novandiramadhan.reflect.ui.theme.ReflectTheme
import com.novandiramadhan.reflect.util.NotificationPermissionUtil
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onSignOut: () -> Unit = {},
    onSendFeedback: () -> Unit = {},
    onReportBug: () -> Unit = {},
    onAboutApp: () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val user by viewModel.user.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val showTimePicker by viewModel.showTimePicker.collectAsState()
    val showSignOutDialog by viewModel.showSignOutDialog.collectAsState()
    val settingState by viewModel.settingState.collectAsState()

    val memberSince = user.createdAt?.toDate()?.let {
        SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(it)
    } ?: "Unknown"

    val selectedLanguageLabel = when (val state = settingState) {
        is SettingUiState.Loading -> "English"
        is SettingUiState.Success -> {
            LaunchedEffect(state.shouldRecreateActivity) {
                if (state.shouldRecreateActivity) {
                    viewModel.onActivityRecreated()
                    activity?.recreate()
                }
            }
            state.availableLanguages.firstOrNull {
                it.code == state.selectedLanguage
            }?.displayLanguage ?: "English"
        }
        is SettingUiState.Error -> {
            LaunchedEffect(state.message) {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            state.availableLanguages.firstOrNull {
                it.code == state.selectedLanguage
            }?.displayLanguage ?: "English"
        }
    }

    val availableLanguages = when (val state = settingState) {
        is SettingUiState.Success -> state.availableLanguages
        is SettingUiState.Error -> state.availableLanguages
        else -> emptyList()
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.setShowTimePicker(true)
        } else {
            Toast.makeText(
                context, context.getString(R.string.notification_denied), Toast.LENGTH_LONG
            ).show()
        }
    }

    val backgroundColor = MaterialTheme.colorScheme.background
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawRect(backgroundColor)
                    drawContent()
                }
        )

        Image(
            painter = painterResource(id = R.drawable.background_doodle),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                backgroundColor,
                                Color.Transparent,
                                Color.Transparent,
                            ),
                            startY = 0f,
                            endY = size.height * 2f
                        )
                    )
                }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${stringResource(R.string.member_since)}: $memberSince",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.app_settings),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SettingItem(
                        icon = Icons.Default.DarkMode,
                        title = stringResource(R.string.dark_mode)
                    ) {
                        Switch(
                            checked = settings.theme == "dark",
                            onCheckedChange = {
                                viewModel.setThemeSetting(if (it) "dark" else "light")
                            }
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    SettingItem(
                        icon = Icons.Default.Notifications,
                        title = stringResource(R.string.reminder_settings)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Switch(
                                modifier = Modifier.align(Alignment.End),
                                checked = settings.reminderTime.isNotEmpty(),
                                onCheckedChange = { isChecked ->
                                    if (isChecked && activity != null) {
                                        if (!NotificationPermissionUtil.hasNotificationPermission(context)) {
                                            NotificationPermissionUtil.requestNotificationPermission(
                                                activity,
                                                notificationPermissionLauncher
                                            )
                                        } else viewModel.setShowTimePicker(true)
                                    } else {
                                        viewModel.cancelDailyReminder()
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.reminder_set_cancel),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )

                            if (settings.reminderTime.isNotEmpty()) {
                                Text(
                                    text = settings.reminderTime,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    SettingItem(
                        icon = Icons.Default.Fingerprint,
                        title = stringResource(R.string.biometrics_settings)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Switch(
                                modifier = Modifier.align(Alignment.End),
                                checked = settings.reminderTime.isNotEmpty(),
                                onCheckedChange = { isChecked ->
                                }
                            )

                            if (settings.reminderTime.isNotEmpty()) {
                                Text(
                                    text = settings.reminderTime,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    SettingItem(
                        icon = Icons.Default.Language,
                        title = stringResource(R.string.language_settings)
                    ) {
                        if (availableLanguages.isNotEmpty()) {
                            RDropdown(
                                modifier = Modifier.width(150.dp),
                                items = availableLanguages.map { it.displayLanguage },
                                selectedItem = selectedLanguageLabel,
                                onItemSelected = { label ->
                                    val lang = availableLanguages.firstOrNull {
                                        it.displayLanguage == label
                                    }?.code ?: "en"
                                    viewModel.setLanguageSetting(lang)
                                }
                            )
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.support_feedback),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ClickableSettingItem(
                        icon = Icons.Default.Feedback,
                        title = stringResource(R.string.send_feedback),
                        onClick = onSendFeedback
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    ClickableSettingItem(
                        icon = Icons.Default.Support,
                        title = stringResource(R.string.report_bug),
                        onClick = onReportBug
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    ClickableSettingItem(
                        icon = Icons.Default.Info,
                        title = stringResource(R.string.about_app),
                        onClick = onAboutApp
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    ClickableSettingItem(
                        icon = Icons.Default.Info,
                        title = stringResource(R.string.contact_developer),
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = "mailto:${
                                    context.getString(R.string.email_developer)
                                }".toUri()
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.setShowSignOutDialog(true)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Row(
                    modifier = Modifier.height(40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        stringResource(R.string.sign_out)
                    )
                    Text(
                        text = stringResource(R.string.sign_out),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }

    if (showTimePicker) {
        val (initialHour, initialMinute) = if (settings.reminderTime.isNotEmpty()) {
            val parts = settings.reminderTime.split(":")
            Pair(parts[0].toIntOrNull() ?: 0, parts.getOrNull(1)?.toIntOrNull() ?: 0)
        } else {
            val now = Calendar.getInstance()
            Pair(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE))
        }
        val timePickerState = rememberTimePickerState(
            initialHour = initialHour,
            initialMinute = initialMinute
        )

        TimePickerDialog(
            state = timePickerState,
            onDismiss = {
                viewModel.setShowTimePicker(false)
            },
            onTimeSelected = { hour, minute ->
                viewModel.setDailyReminder(hour, minute)
                Toast.makeText(
                    context,
                    context.getString(R.string.reminder_set_success, "$hour:$minute"),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.setShowTimePicker(false)
            }
        )
    }

    if (showSignOutDialog) {
        ConfirmationDialog(
            icon = Icons.AutoMirrored.Rounded.Logout,
            title = stringResource(R.string.sign_out),
            message = stringResource(R.string.sign_out_description),
            confirmText = stringResource(R.string.sign_out),
            dismissText = stringResource(R.string.cancel),
            onConfirm = {
                viewModel.setShowSignOutDialog(false)
                viewModel.logout()
                onSignOut()
            },
            onDismiss = {
                viewModel.setShowSignOutDialog(false)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ReflectTheme {
        ProfileScreen()
    }
}
