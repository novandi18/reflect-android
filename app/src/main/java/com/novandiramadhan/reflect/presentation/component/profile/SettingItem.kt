package com.novandiramadhan.reflect.presentation.component.profile

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        )

        content()
    }
}

@Composable
fun ClickableSettingItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClickableSettingItemPreview() {
    ReflectTheme {
        ClickableSettingItem(
            icon = ImageVector.vectorResource(id = android.R.drawable.ic_menu_delete),
            title = "Delete Account",
            onClick = {},
            iconTint = Color.Red,
            textColor = Color.Red
        )
    }
}