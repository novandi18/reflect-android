package com.novandiramadhan.reflect.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.data.mapper.NotificationMapper
import com.novandiramadhan.reflect.domain.model.Notification
import com.novandiramadhan.reflect.presentation.component.notifications.NotificationCard
import com.novandiramadhan.reflect.presentation.viewmodel.NotificationViewModel
import com.novandiramadhan.reflect.ui.theme.ReflectTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = hiltViewModel(),
    onNotificationClick: (Notification) -> Unit = {}
) {
    val user by viewModel.user.collectAsState()
    val notificationsPaging = viewModel.notifications.collectAsLazyPagingItems()
    val coroutineScope = rememberCoroutineScope()
    val refreshState = rememberPullToRefreshState()
    val isRefreshing = notificationsPaging.loadState.refresh is LoadState.Loading


    val backgroundColor = MaterialTheme.colorScheme.background

    PullToRefreshBox(
        modifier = Modifier.fillMaxWidth(),
        state = refreshState,
        isRefreshing = isRefreshing,
        onRefresh = {
            if (user.id.isNotEmpty()) viewModel.refreshNotifications(user.id)
        },
        indicator = {
            Indicator(
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                color = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary,
            )
        }
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_doodle),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                backgroundColor,
                                Color.Transparent,
                                Color.Transparent,
                            ),
                            startY = 0f,
                            endY = size.height * 2f
                        )
                    )
                }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (notificationsPaging.itemCount == 0) {
                EmptyNotifications(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(notificationsPaging.itemCount) { index ->
                        val notificationEntity = notificationsPaging[index]
                        notificationEntity?.let {
                            val notification = NotificationMapper.entityToDomain(notificationEntity)
                            val dismissState = rememberSwipeToDismissBoxState()

                            SwipeToDismissBox(
                                state = dismissState,
                                backgroundContent = {
                                    val direction = dismissState.dismissDirection
                                    val progress = dismissState.progress

                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 32.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        if (direction == SwipeToDismissBoxValue.EndToStart) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = MaterialTheme.colorScheme.error.copy(
                                                    alpha = progress.coerceIn(0f, 1f)
                                                ),
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                },
                                enableDismissFromStartToEnd = false, // Disable swipe to mark as read
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                NotificationCard(
                                    notification = notification,
                                    onNotificationClick = onNotificationClick
                                )
                            }

                            if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                                coroutineScope.launch {
                                    viewModel.deleteNotification(user.id, notification.id)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyNotifications(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = null,
            modifier = Modifier
                .size(72.dp)
                .padding(bottom = 16.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )

        Text(
            text = stringResource(R.string.no_notifications),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.no_notifications_desc),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    ReflectTheme {
        NotificationScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyNotificationScreenPreview() {
    ReflectTheme {
        NotificationScreen()
    }
}