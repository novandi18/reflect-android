package com.novandiramadhan.reflect.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.novandiramadhan.reflect.presentation.screen.MoodEntryScreen

fun NavGraphBuilder.entryGraph(navController: NavController) {
    composable<Destinations.MoodEntry> {
        MoodEntryScreen()
    }
}