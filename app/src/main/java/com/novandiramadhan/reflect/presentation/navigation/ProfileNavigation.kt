package com.novandiramadhan.reflect.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.novandiramadhan.reflect.presentation.screen.AboutScreen
import com.novandiramadhan.reflect.presentation.screen.FeedbackScreen
import com.novandiramadhan.reflect.presentation.screen.ReportBugScreen

fun NavGraphBuilder.profileGraph() {
    composable<Destinations.About> {
        AboutScreen()
    }
    composable<Destinations.Feedback> {
        FeedbackScreen()
    }
    composable<Destinations.ReportBug> {
        ReportBugScreen()
    }
}