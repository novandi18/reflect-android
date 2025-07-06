package com.novandiramadhan.reflect.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.presentation.navigation.Destinations
import com.novandiramadhan.reflect.ui.theme.ReflectTheme
import com.novandiramadhan.reflect.util.BottomBarItems

@Composable
fun ReflectBottomBar(
    currentRoute: NavDestination? = null,
    visible: Boolean,
    navigate: (Destinations) -> Unit
) {
    val bottomBarItems = BottomBarItems().getItems()

    val isInsightSelected = currentRoute?.hierarchy?.any {
        it.hasRoute(Destinations.Insight::class)
    } == true

    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = navigationBarPadding.calculateBottomPadding()),
            color = MaterialTheme.colorScheme.background,
            shadowElevation = 0.dp,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    bottomBarItems.take(2).forEach { item ->
                        BottomBarItem(
                            item = item,
                            selected = (currentRoute?.hierarchy?.any {
                                it.hasRoute(item.route::class)
                            }) == true,
                            onItemClick = { navigate(item.route) }
                        )
                    }
                }

                Box(contentAlignment = Alignment.Center) {
                    FloatingActionButton(
                        onClick = { navigate(Destinations.Insight) },
                        shape = CircleShape,
                        containerColor = if (isInsightSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(if (isInsightSelected) 64.dp else 58.dp),
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = if (isInsightSelected) 4.dp else 0.dp,
                            pressedElevation = 0.dp,
                            hoveredElevation = 0.dp,
                            focusedElevation = 0.dp,
                        )
                    ) {
                        Icon(
                            imageVector = bottomBarItems[2].icon, // Insight icon (3rd item)
                            contentDescription = stringResource(R.string.record_mood_button),
                            modifier = Modifier.size(if (isInsightSelected) 32.dp else 28.dp)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    bottomBarItems.takeLast(2).forEach { item ->
                        BottomBarItem(
                            item = item,
                            selected = (currentRoute?.hierarchy?.any {
                                it.hasRoute(item.route::class)
                            }) == true,
                            onItemClick = { navigate(item.route) }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ReflectBottomBarPreview() {
    ReflectTheme {
        ReflectBottomBar(
            visible = true,
            navigate = {}
        )
    }
}
