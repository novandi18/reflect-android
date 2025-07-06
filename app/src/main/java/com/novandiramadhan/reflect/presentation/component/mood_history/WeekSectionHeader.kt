package com.novandiramadhan.reflect.presentation.component.mood_history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.ui.theme.ReflectTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WeekSectionHeader(firstDate: Date, lastDate: Date) {
    val firstFormatter = SimpleDateFormat("MMM d", Locale.getDefault())
    val lastFormatter = SimpleDateFormat("d, yyyy", Locale.getDefault())

    val dateRange = "${firstFormatter.format(firstDate)} - ${lastFormatter.format(lastDate)}"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = dateRange,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
@Preview
private fun Preview() {
    ReflectTheme {
        WeekSectionHeader(firstDate = Date(), lastDate = Date())
    }
}
