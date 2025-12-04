package com.novandiramadhan.reflect.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Destinations(
    val showBottomBar: Boolean
) {
    @Serializable
    data object Welcome: Destinations(
        showBottomBar = false
    )

    @Serializable
    data object SignIn: Destinations(
        showBottomBar = false
    )

    @Serializable
    data object SignUp: Destinations(
        showBottomBar = false
    )

    @Serializable
    data object Profile: Destinations(
        showBottomBar = true
    )

    @Serializable
    data object Home: Destinations(
        showBottomBar = true
    )

    @Serializable
    data object Insight: Destinations(
        showBottomBar = true
    )

    @Serializable
    data object MoodHistory: Destinations(
        showBottomBar = true
    )

    @Serializable
    data object MoodEntry: Destinations(
        showBottomBar = false
    )

    @Serializable
    data object Notifications: Destinations(
        showBottomBar = true
    )

    @Serializable
    data object Settings: Destinations(
        showBottomBar = false
    )

    @Serializable
    data object About: Destinations(
        showBottomBar = false
    )

    @Serializable
    data object Feedback: Destinations(
        showBottomBar = false
    )

    @Serializable
    data object ReportBug: Destinations(
        showBottomBar = false
    )

    companion object {
         val destinations = listOf(Welcome, SignIn, SignUp, Home, Insight, Profile, MoodEntry,
             MoodHistory, Notifications, Settings, About, Feedback, ReportBug)
    }
}