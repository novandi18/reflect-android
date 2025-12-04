package com.novandiramadhan.reflect.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.novandiramadhan.reflect.presentation.component.ReflectBottomBar
import com.novandiramadhan.reflect.presentation.component.TopAppBarSelector
import com.novandiramadhan.reflect.presentation.helper.RouteAwareCompositionLocalProvider
import com.novandiramadhan.reflect.presentation.navigation.Destinations
import com.novandiramadhan.reflect.presentation.navigation.authGraph
import com.novandiramadhan.reflect.presentation.navigation.entryGraph
import com.novandiramadhan.reflect.presentation.navigation.mainGraph
import com.novandiramadhan.reflect.presentation.navigation.profileGraph
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@Composable
fun ReflectApp(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    startDestination: Destinations = Destinations.Welcome
) {
    val navBackStackEntry = navHostController.currentBackStackEntryAsState().value
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = Destinations.destinations.filterNotNull().any { dest ->
        currentDestination?.hierarchy?.any { destination ->
            destination.hasRoute(dest::class) && dest.showBottomBar
        } == true
    }

    RouteAwareCompositionLocalProvider(currentDestination) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBarSelector(
                    navHostController = navHostController,
                    currentRoute = currentDestination
                )
            },
            bottomBar = {
                ReflectBottomBar(
                    currentRoute = currentDestination,
                    visible = showBottomBar,
                    navigate = { destination ->
                        navHostController.navigate(destination) {
                            popUpTo(Destinations.Welcome) {
                                saveState = true
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            NavHost(
                modifier = Modifier.padding(paddingValues),
                navController = navHostController,
                startDestination = startDestination,
            ) {
                authGraph(navController = navHostController)
                mainGraph(navController = navHostController)
                entryGraph(navController = navHostController)
                profileGraph()
            }
        }
    }
}

@Composable
@androidx.compose.ui.tooling.preview.Preview
fun ReflectAppPreview() {
    ReflectTheme {
        ReflectApp(
            startDestination = Destinations.Welcome
        )
    }
}
