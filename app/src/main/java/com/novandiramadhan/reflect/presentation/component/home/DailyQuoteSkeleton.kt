package com.novandiramadhan.reflect.presentation.component.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@Composable
fun DailyQuoteSkeleton(
    modifier: Modifier = Modifier
) {
    val alphaAnim by rememberInfiniteTransition(label = "skeleton").animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "skeletonAnim"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = CardDefaults.shape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            SkeletonBox(
                modifier = Modifier
                    .width(100.dp)
                    .height(20.dp)
                    .alpha(alphaAnim)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.Top
            ) {
                SkeletonBox(
                    modifier = Modifier
                        .size(24.dp)
                        .alpha(alphaAnim),
                    shape = CircleShape
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    SkeletonBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(18.dp)
                            .alpha(alphaAnim)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    SkeletonBox(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(16.dp)
                            .alpha(alphaAnim)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    SkeletonBox(
                        modifier = Modifier
                            .align(Alignment.End)
                            .width(80.dp)
                            .height(12.dp)
                            .alpha(alphaAnim)
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
fun DailyQuoteSkeletonPreview() {
    ReflectTheme {
        DailyQuoteSkeleton(
            modifier = Modifier.padding(16.dp)
        )
    }
}