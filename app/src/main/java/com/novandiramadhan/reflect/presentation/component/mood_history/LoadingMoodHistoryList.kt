package com.novandiramadhan.reflect.presentation.component.mood_history

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@Composable
fun LoadingMoodHistoryList() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(5) {
            ShimmerMoodHistoryItem()
        }
    }
}

@Composable
private fun ShimmerMoodHistoryItem() {
    val shimmerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
    val transition = rememberInfiniteTransition(label = "shimmer")
    val alpha by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerAlpha"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(16.dp)
                    .background(shimmerColor.copy(alpha = alpha), RoundedCornerShape(4.dp))
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(shimmerColor.copy(alpha = alpha), CircleShape)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(20.dp)
                            .background(shimmerColor.copy(alpha = alpha), RoundedCornerShape(4.dp))
                    )
                }

                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(20.dp)
                        .background(shimmerColor.copy(alpha = alpha), RoundedCornerShape(16.dp))
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(20.dp)
                        .background(shimmerColor.copy(alpha = alpha), RoundedCornerShape(12.dp))
                )

                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(20.dp)
                        .background(shimmerColor.copy(alpha = alpha), RoundedCornerShape(12.dp))
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .background(shimmerColor.copy(alpha = alpha), RoundedCornerShape(4.dp))
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(16.dp)
                    .background(shimmerColor.copy(alpha = alpha), RoundedCornerShape(4.dp))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingMoodHistoryListPreview() {
    ReflectTheme(
        darkTheme = true
    ) {
        LoadingMoodHistoryList()
    }
}
