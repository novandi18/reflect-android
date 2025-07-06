package com.novandiramadhan.reflect.util

import com.novandiramadhan.reflect.presentation.navigation.NavigationItem

class BottomBarItems {
    fun getItems(): List<NavigationItem> = listOf(
        NavigationItem.Home,
        NavigationItem.MoodHistory,
        NavigationItem.Insight,
        NavigationItem.Notifications,
        NavigationItem.Profile,
    )
}