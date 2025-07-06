package com.novandiramadhan.reflect.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.presentation.component.RButton
import com.novandiramadhan.reflect.presentation.component.welcome.OnboardingItem
import com.novandiramadhan.reflect.presentation.component.welcome.OnboardingSlide
import com.novandiramadhan.reflect.presentation.viewmodel.WelcomeViewModel
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel = hiltViewModel(),
    start: () -> Unit = {},
) {
    val onboardingItems = listOf(
        OnboardingItem(
            image = R.drawable.welcome,
            titleRes = R.string.welcome_slide_title_1,
            descriptionRes = R.string.welcome_slide_description_1
        ),
        OnboardingItem(
            image = R.drawable.track_moods,
            titleRes = R.string.welcome_slide_title_2,
            descriptionRes = R.string.welcome_slide_description_2
        ),
        OnboardingItem(
            image = R.drawable.discover_emotional,
            titleRes = R.string.welcome_slide_title_3,
            descriptionRes = R.string.welcome_slide_description_3
        )
    )

    val pagerState = rememberPagerState(pageCount = { 3 })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                OnboardingSlide(item = onboardingItems[page])
            }

            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color = if (pagerState.currentPage == iteration)
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onBackground.copy(.3f)
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                    )
                }
            }

            AnimatedVisibility(
                visible = pagerState.currentPage == onboardingItems.size - 1,
                modifier = Modifier.padding(24.dp)
            ) {
                RButton(
                    text = stringResource(R.string.btn_continue),
                    onClick = {
                        viewModel.setState(true)
                        start()
                    },
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomeScreenPreview() {
    ReflectTheme {
        WelcomeScreen()
    }
}