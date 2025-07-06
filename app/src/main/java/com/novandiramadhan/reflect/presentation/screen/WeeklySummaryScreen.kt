package com.novandiramadhan.reflect.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@Composable
fun WeeklySummaryScreen(
    back: () -> Unit = {},
) {
}

@Preview(showBackground = true)
@Composable
private fun WeeklySummaryScreenPreview() {
    ReflectTheme {
        WeeklySummaryScreen()
    }
}