package com.novandiramadhan.reflect.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.ui.graphics.vector.ImageVector
import com.novandiramadhan.reflect.R

sealed class NavigationItem(
    val route: Destinations,
    val icon: ImageVector,
    val title: Int,
    val selectedIcon: ImageVector,
) {
    data object Home: NavigationItem(
        route = Destinations.Home,
        icon = Icons.Outlined.Home,
        title = R.string.home,
        selectedIcon = Icons.Filled.Home
    )

    data object MoodHistory: NavigationItem(
        route = Destinations.MoodHistory,
        icon = Icons.Outlined.Receipt,
        title = R.string.mood_history,
        selectedIcon = Icons.Filled.Receipt
    )

    data object Insight: NavigationItem(
        route = Destinations.Insight,
        icon = Icons.Outlined.Insights,
        title = R.string.insight,
        selectedIcon = Icons.Filled.Insights
    )

    data object Notifications: NavigationItem(
        route = Destinations.Notifications,
        icon = Icons.Outlined.Notifications,
        title = R.string.notifications,
        selectedIcon = Icons.Filled.Notifications
    )

    data object Profile: NavigationItem(
        route = Destinations.Profile,
        icon = Icons.Outlined.Person,
        title = R.string.profile,
        selectedIcon = Icons.Filled.Person
    )
}