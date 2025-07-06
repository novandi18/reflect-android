package com.novandiramadhan.reflect.presentation.component.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

data class OnboardingItem(
    val image: Int,
    val titleRes: Int,
    val descriptionRes: Int
)

@Composable
fun OnboardingSlide(item: OnboardingItem) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = item.image),
            contentDescription = null,
            modifier = Modifier
                .size(280.dp)
                .padding(bottom = 32.dp)
        )

        Text(
            text = stringResource(id = item.titleRes),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = stringResource(id = item.descriptionRes),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingSlidePreview() {
    ReflectTheme {
        OnboardingSlide(
            item = OnboardingItem(
                image = R.drawable.welcome,
                titleRes = R.string.welcome_slide_title_1,
                descriptionRes = R.string.welcome_slide_description_1
            )
        )
    }
}
