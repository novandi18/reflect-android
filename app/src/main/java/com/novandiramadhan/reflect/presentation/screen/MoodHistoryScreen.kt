package com.novandiramadhan.reflect.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.data.mapper.MoodHistoryMapper.toJournal
import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.presentation.component.ConfirmationDialog
import com.novandiramadhan.reflect.presentation.component.mood_history.EmptyMoodHistoryState
import com.novandiramadhan.reflect.presentation.component.mood_history.ErrorMoodHistoryState
import com.novandiramadhan.reflect.presentation.component.mood_history.LoadingMoodHistoryList
import com.novandiramadhan.reflect.presentation.component.mood_history.MoodHistoryCard
import com.novandiramadhan.reflect.presentation.helper.getRouteViewModel
import com.novandiramadhan.reflect.presentation.viewmodel.MoodHistoryViewModel
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodHistoryScreen(
    viewModel: MoodHistoryViewModel = getRouteViewModel<MoodHistoryViewModel>()
) {
    val context = LocalContext.current
    val deleteResult by viewModel.deleteResult.collectAsState()
    val moodHistory = viewModel.moodHistory.collectAsLazyPagingItems()
    val deleteDialog by viewModel.deleteDialog.collectAsState()
    val refreshState = rememberPullToRefreshState()
    val isRefreshing = moodHistory.loadState.refresh is LoadState.Loading

    deleteResult?.let {
        when (it) {
            is Resource.Success -> {
                viewModel.clearDeleteResult()
                Toast.makeText(
                    context,
                    deleteResult!!.data,
                    Toast.LENGTH_SHORT
                ).show()
            }
            is Resource.Error -> {
                viewModel.clearDeleteResult()
                Toast.makeText(
                    context,
                    deleteResult!!.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {}
        }
    }

    if (deleteDialog.first) {
        ConfirmationDialog(
            icon = Icons.Rounded.DeleteForever,
            title = stringResource(R.string.delete_mood_history_title),
            message = stringResource(R.string.delete_mood_history_description),
            confirmText = stringResource(R.string.confirm),
            dismissText = stringResource(R.string.cancel),
            onConfirm = {
                viewModel.setDeleteDialog(false, null)
                viewModel.deleteMoodHistory(deleteDialog.second!!)
            },
            onDismiss = { viewModel.setDeleteDialog(false, null) }
        )
    }

    val backgroundColor = MaterialTheme.colorScheme.background
    PullToRefreshBox(
        modifier = Modifier.fillMaxWidth(),
        state = refreshState,
        onRefresh = moodHistory::refresh,
        isRefreshing = isRefreshing,
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

        when {
            moodHistory.loadState.refresh is LoadState.Loading -> {
                LoadingMoodHistoryList()
            }

            moodHistory.loadState.refresh is LoadState.Error -> {
                val error = (moodHistory.loadState.refresh as LoadState.Error).error
                ErrorMoodHistoryState(
                    message = error.message.toString(),
                    onRetry = {
                        moodHistory.retry()
                    }
                )
            }

            moodHistory.itemCount == 0 -> {
                EmptyMoodHistoryState()
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        count = moodHistory.itemCount,
                        key = { i -> moodHistory[i]?.documentId ?: 0 }
                    ) { index ->
                        val entity = moodHistory[index]
                        if (entity != null) {
                            MoodHistoryCard(
                                journal = entity.toJournal(),
                                onClick = {},
                                onDelete = {
                                    viewModel.setDeleteDialog(true, entity)
                                }
                            )
                        }
                    }

                    if (moodHistory.loadState.append is LoadState.Loading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center),
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoodHistoryScreenPreview() {
    ReflectTheme {
        MoodHistoryScreen()
    }
}

