package com.novandiramadhan.reflect.presentation.component.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.domain.model.MoodOption
import com.novandiramadhan.reflect.ui.theme.LightGreen
import com.novandiramadhan.reflect.ui.theme.Purple
import com.novandiramadhan.reflect.ui.theme.ReflectTheme
import com.novandiramadhan.reflect.ui.theme.White

@Composable
fun MoodItem(
    mood: MoodOption,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onSelect() }
            .padding(4.dp)
    ) {
        Card(
            modifier = Modifier.size(56.dp),
            shape = CardDefaults.shape,
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected)
                    mood.color
                else MaterialTheme.colorScheme.background
            ),
            border = BorderStroke(1.dp, mood.color)
        ) {
            Column(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        if (isSelected) mood.color
                        else mood.color.copy(alpha = .15f)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = mood.icon,
                    contentDescription = mood.label,
                    tint = if (isSelected)
                        White
                    else
                        mood.color
                )
            }
        }

        Text(
            text = mood.label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(top = 4.dp)
        )
    }
}

@Preview
@Composable
fun MoodItemPreview() {
    ReflectTheme(
        darkTheme = true
    ) {
        MoodItem(
            mood = MoodOption(
                level = 5,
                icon = Icons.Default.Email,
                label = "Happy",
                color = Purple
            ),
            isSelected = false,
            onSelect = {}
        )
    }
}

