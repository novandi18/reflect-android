package com.novandiramadhan.reflect.presentation.component.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@Composable
fun WeeklyStatsCardSkeleton(
    modifier: Modifier = Modifier
) {
    val alphaAnim = rememberInfiniteTransition(label = "skeleton").animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "skeletonAnim"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            SkeletonBox(
                modifier = Modifier
                    .width(120.dp)
                    .height(20.dp)
                    .alpha(alphaAnim.value)
            )
            Spacer(modifier = Modifier.height(12.dp))

            SkeletonBox(
                modifier = Modifier
                    .width(100.dp)
                    .height(16.dp)
                    .alpha(alphaAnim.value)
            )

            Spacer(modifier = Modifier.height(8.dp))

            SkeletonBox(
                modifier = Modifier
                    .width(180.dp)
                    .height(12.dp)
                    .alpha(alphaAnim.value)
            )
            Spacer(modifier = Modifier.height(16.dp))

            SkeletonBox(
                modifier = Modifier
                    .width(100.dp)
                    .height(16.dp)
                    .alpha(alphaAnim.value)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(4) {
                    SkeletonBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .alpha(alphaAnim.value)
                    )
                }
            }
        }
    }
}

@Composable
private fun SkeletonBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(6.dp),
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = .7f)
) {
    Box(
        modifier = modifier
            .background(color = color, shape = shape)
    )
}

@Preview(showBackground = true)
@Composable
fun WeeklyStatsCardSkeletonPreview() {
    ReflectTheme(
        darkTheme = true
    ) {
        WeeklyStatsCardSkeleton(
            modifier = Modifier.padding(16.dp)
        )
    }
}