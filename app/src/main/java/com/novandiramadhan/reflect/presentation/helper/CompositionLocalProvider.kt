package com.novandiramadhan.reflect.presentation.helper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDestination
import com.novandiramadhan.reflect.presentation.navigation.Destinations
import com.novandiramadhan.reflect.presentation.viewmodel.MoodHistoryViewModel
import com.novandiramadhan.reflect.presentation.viewmodel.NotificationViewModel
import kotlin.reflect.KClass

val LocalViewModelRegistry = compositionLocalOf<Map<KClass<out ViewModel>, ViewModel>> { emptyMap() }

private val destinationToViewModelProviders: Map<KClass<out Destinations>, List<@Composable () -> Pair<KClass<out ViewModel>, ViewModel>>> = mapOf(
    Destinations.MoodHistory::class to listOf(
        { MoodHistoryViewModel::class to hiltViewModel<MoodHistoryViewModel>() },
    ),
    Destinations.Notifications::class to listOf(
        { NotificationViewModel::class to hiltViewModel<NotificationViewModel>() },
    )
)

@Composable
fun RouteAwareCompositionLocalProvider(
    currentDestination: NavDestination?,
    content: @Composable () -> Unit
) {
    val registry = remember {
        mutableMapOf<KClass<out ViewModel>, ViewModel>()
    }

    if (currentDestination != null) {
        destinationToViewModelProviders.forEach { (destinationClass, providers) ->
            val routeMatches = currentDestination.route?.contains(destinationClass.simpleName ?: "") == true

            if (routeMatches) {
                providers.forEach { provider ->
                    val (vmClass, vm) = provider()
                    registry[vmClass] = vm
                }
            }
        }
    }

    CompositionLocalProvider(
        LocalViewModelRegistry provides registry,
        content = content
    )
}

@Composable
inline fun <reified VM : ViewModel> getRouteViewModel(): VM {
    val registry = LocalViewModelRegistry.current
    return registry[VM::class] as? VM
        ?: error("ViewModel for ${VM::class.simpleName} not found in LocalViewModelRegistry.")
}