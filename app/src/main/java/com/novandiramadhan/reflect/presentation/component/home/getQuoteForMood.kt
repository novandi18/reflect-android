package com.novandiramadhan.reflect.presentation.component.home

import androidx.compose.runtime.Composable
import com.novandiramadhan.reflect.domain.model.Quote

@Composable
fun getQuoteForMood(mood: String): Quote {
    return when (mood.lowercase()) {
        "very sad", "sad" -> Quote(
            quoteText = "In the middle of winter I at last discovered that there was in me an invincible summer.",
            source = "Albert Camus"
        )
        "disappointed" -> Quote(
            quoteText = "Disappointment is a sort of bankruptcy - the bankruptcy of a soul that expends too much in hope and expectation.",
            source = "Eric Hoffer"
        )
        "uncomfortable", "okay" -> Quote(
            quoteText = "Life begins at the end of your comfort zone.",
            source = "Neale Donald Walsch"
        )
        "pretty good", "good" -> Quote(
            quoteText = "Happiness is not something ready-made. It comes from your own actions.",
            source = "Dalai Lama"
        )
        "happy", "very happy" -> Quote(
            quoteText = "The most beautiful things in the world cannot be seen or even touched. They must be felt with the heart.",
            source = "Helen Keller"
        )
        "joyful" -> Quote(
            quoteText = "Joy does not simply happen to us. We have to choose joy and keep choosing it every day.",
            source = "Henri Nouwen"
        )
        else -> Quote(
            quoteText = "Every day is a new beginning. Take a deep breath and start again.",
            source = "Unknown"
        )
    }
}