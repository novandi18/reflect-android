package com.novandiramadhan.reflect.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import com.novandiramadhan.reflect.presentation.component.about.AboutTopBar
import com.novandiramadhan.reflect.presentation.component.feedback.FeedbackTopBar
import com.novandiramadhan.reflect.presentation.component.home.HomeTopBar
import com.novandiramadhan.reflect.presentation.component.insight.InsightTopBar
import com.novandiramadhan.reflect.presentation.component.mood_entry.MoodEntryTopBar
import com.novandiramadhan.reflect.presentation.component.mood_history.MoodHistoryTopBar
import com.novandiramadhan.reflect.presentation.component.notifications.NotificationTopBar
import com.novandiramadhan.reflect.presentation.component.report_bug.ReportBugTopBar
import com.novandiramadhan.reflect.presentation.navigation.Destinations
import androidx.compose.runtime.getValue
import androidx.paging.compose.collectAsLazyPagingItems
import com.novandiramadhan.reflect.presentation.component.summary.MonthlySummaryTopBar
import com.novandiramadhan.reflect.presentation.component.summary.WeeklySummaryTopBar
import com.novandiramadhan.reflect.presentation.helper.getRouteViewModel
import com.novandiramadhan.reflect.presentation.viewmodel.MoodHistoryViewModel
import com.novandiramadhan.reflect.presentation.viewmodel.NotificationViewModel

@Composable
fun TopAppBarSelector(
    navHostController: NavHostController,
    currentRoute: NavDestination? = null
) {
    when {
        currentRoute?.hasRoute(Destinations.Home::class) == true -> {
            HomeTopBar()
        }
        currentRoute?.hasRoute(Destinations.Insight::class) == true -> {
            InsightTopBar()
        }
        currentRoute?.hasRoute(Destinations.MoodHistory::class) == true -> {
            val viewModel = getRouteViewModel<MoodHistoryViewModel>()
            val filterState by viewModel.filterState.collectAsState()
            val showFilterDialog by viewModel.showFilterDialog.collectAsState()

            MoodHistoryTopBar(
                showFilterDialog = showFilterDialog,
                filterState = filterState,
                onShowFilterDialog = viewModel::setShowFilterDialog,
                onFilterChange = viewModel::setFilterState
            )
        }
        currentRoute?.hasRoute(Destinations.Notifications::class) == true -> {
            val viewModel = getRouteViewModel<NotificationViewModel>()
            val notificationsPaging = viewModel.notifications.collectAsLazyPagingItems()
            val hasNotifications = notificationsPaging.itemCount > 0

            NotificationTopBar(
                showActions = hasNotifications,
                onReadAllClick = {
                    viewModel.markAllAsRead()
                },
                onDeleteAllClick = {
                    viewModel.deleteAllNotifications()
                }
            )
        }
        currentRoute?.hasRoute(Destinations.MoodEntry::class) == true -> {
            MoodEntryTopBar(
                back = {
                    navHostController.popBackStack()
                }
            )
        }
        currentRoute?.hasRoute(Destinations.About::class) == true -> {
            AboutTopBar(
                back = {
                    navHostController.popBackStack()
                }
            )
        }
        currentRoute?.hasRoute(Destinations.Feedback::class) == true -> {
            FeedbackTopBar(
                back = {
                    navHostController.popBackStack()
                }
            )
        }
        currentRoute?.hasRoute(Destinations.ReportBug::class) == true -> {
            ReportBugTopBar(
                back = {
                    navHostController.popBackStack()
                }
            )
        }
        currentRoute?.hasRoute(Destinations.WeeklySummary::class) == true -> {
            WeeklySummaryTopBar(
                back = {
                    navHostController.popBackStack()
                }
            )
        }
        currentRoute?.hasRoute(Destinations.MonthlySummary::class) == true -> {
            MonthlySummaryTopBar(
                back = {
                    navHostController.popBackStack()
                }
            )
        }
    }
}