package com.novandiramadhan.reflect.presentation.component.profile

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.presentation.component.RTextField
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelector(
    expanded: Boolean,
    selectedLanguage: String,
    onExpandChange: (Boolean) -> Unit,
    onLanguageSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Language,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = stringResource(R.string.language_settings),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandChange,
        ) {
            RTextField(
                value = if (selectedLanguage == "en") {
                    stringResource(R.string.language_english)
                } else {
                    stringResource(R.string.language_indonesian)
                },
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                    .width(140.dp),
                singleLine = true
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandChange(false) },
                containerColor = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp,
                tonalElevation = 2.dp
            ) {
                DropdownMenuItem(
                    text = {
                        Text(stringResource(R.string.language_english))
                    },
                    onClick = {
                        onLanguageSelected("en")
                        onExpandChange(false)
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(stringResource(R.string.language_indonesian))
                    },
                    onClick = {
                        onLanguageSelected("id")
                        onExpandChange(false)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LanguageSelectorPreview() {
    ReflectTheme {
        LanguageSelector(
            expanded = false,
            selectedLanguage = "en",
            onExpandChange = {},
            onLanguageSelected = {}
        )
    }
}