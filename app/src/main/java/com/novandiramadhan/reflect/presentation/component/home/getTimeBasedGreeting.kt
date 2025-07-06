package com.novandiramadhan.reflect.presentation.component.home

import android.os.Build
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material.icons.rounded.WbTwilight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import com.novandiramadhan.reflect.R
import java.time.LocalTime
import java.util.Calendar

data class TimeBasedGreeting(
    val greeting: String,
    val icon: ImageVector
)

@Composable
fun getTimeBasedGreeting(): TimeBasedGreeting {
    val currentHour = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalTime.now().hour
        } else {
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        }
    }

    return when {
        currentHour < 12 -> TimeBasedGreeting(
            greeting = stringArrayResource(R.array.greetings)[0],
            icon = Icons.Rounded.WbSunny
        )
        currentHour < 17 -> TimeBasedGreeting(
            greeting = stringArrayResource(R.array.greetings)[1],
            icon = Icons.Rounded.WbSunny
        )
        currentHour < 21 -> TimeBasedGreeting(
            greeting = stringArrayResource(R.array.greetings)[2],
            icon = Icons.Rounded.WbTwilight
        )
        else -> TimeBasedGreeting(
            greeting = stringArrayResource(R.array.greetings)[3],
            icon = Icons.Rounded.NightsStay
        )
    }
}