package com.novandiramadhan.reflect.presentation.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.presentation.navigation.NavigationItem
import com.novandiramadhan.reflect.ui.theme.Green
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@Composable
fun BottomBarItem(
    item: NavigationItem,
    selected: Boolean,
    onItemClick: () -> Unit
) {
    val iconSize by animateDpAsState(
        targetValue = if (selected) 32.dp else 24.dp,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "iconSize"
    )

    Box(
        modifier = Modifier.padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onItemClick,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (selected)
                        Green.copy(alpha = 0.2f)
                    else
                        MaterialTheme.colorScheme.background
                )
                .padding(4.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (selected) item.selectedIcon else item.icon,
                    contentDescription = stringResource(id = item.title),
                    tint = if (selected)
                        Green
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarItemPreview() {
    ReflectTheme {
        BottomBarItem(
            item = NavigationItem.Home,
            selected = false,
            onItemClick = {}
        )
    }
}

