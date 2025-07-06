package com.novandiramadhan.reflect.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.novandiramadhan.reflect.presentation.screen.HomeScreen
import com.novandiramadhan.reflect.presentation.screen.InsightScreen
import com.novandiramadhan.reflect.presentation.screen.MoodHistoryScreen
import com.novandiramadhan.reflect.presentation.screen.NotificationScreen
import com.novandiramadhan.reflect.presentation.screen.ProfileScreen
import com.novandiramadhan.reflect.presentation.screen.WelcomeScreen

fun NavGraphBuilder.mainGraph(navController: NavController) {
    composable<Destinations.Welcome> {
        WelcomeScreen(
            start = {
                navController.navigate(Destinations.SignIn) {
                    popUpTo(Destinations.Welcome) {
                        inclusive = true
                    }
                }
            }
        )
    }

    composable<Destinations.Home> {
        HomeScreen(
            navigate = { destination ->
                navController.navigate(destination)
            }
        )
    }

    composable<Destinations.Insight> {
        InsightScreen()
    }

    composable<Destinations.MoodHistory> {
        MoodHistoryScreen()
    }

    composable<Destinations.Profile> {
        ProfileScreen(
            onSignOut = {
                navController.navigate(Destinations.SignIn) {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            },
            onAboutApp = {
                navController.navigate(Destinations.About)
            },
            onSendFeedback = {
                navController.navigate(Destinations.Feedback)
            },
            onReportBug = {
                navController.navigate(Destinations.ReportBug)
            }
        )
    }

    composable<Destinations.Notifications> {
        NotificationScreen()
    }
}