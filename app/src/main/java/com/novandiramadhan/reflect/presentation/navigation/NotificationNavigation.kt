package com.novandiramadhan.reflect.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.novandiramadhan.reflect.presentation.screen.MonthlySummaryScreen
import com.novandiramadhan.reflect.presentation.screen.WeeklySummaryScreen

fun NavGraphBuilder.notificationGraph() {
    composable<Destinations.WeeklySummary> {
        WeeklySummaryScreen()
    }

    composable<Destinations.MonthlySummary> {
        MonthlySummaryScreen()
    }
}