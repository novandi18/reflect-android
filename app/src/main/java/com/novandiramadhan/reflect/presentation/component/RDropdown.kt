package com.novandiramadhan.reflect.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@Composable
fun RDropdown(
    items: List<String>,
    selectedItem: String?,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
    borderColor: Color = Color.Transparent,
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val filteredItems = if (items.size > 10 && searchQuery.isNotBlank())
        items.filter { it.contains(searchQuery, ignoreCase = true) }
    else items

    Column(modifier = modifier) {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        Box {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(6.dp))
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable(enabled = enabled) { expanded = true }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedItem ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (selectedItem != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                    searchQuery = ""
                    focusManager.clearFocus()
                },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                if (items.size > 10) {
                    RTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = "Search...",
                        singleLine = true,
                        imeAction = ImeAction.Done,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
                if (filteredItems.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("No results", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                        onClick = {},
                        enabled = false
                    )
                } else {
                    filteredItems.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                onItemSelected(item)
                                expanded = false
                                searchQuery = ""
                                focusManager.clearFocus()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RDropdownPreview() {
    ReflectTheme {
        val items = List(20) { "Option ${it + 1}" }
        var selected by remember { mutableStateOf<String?>(items[0]) }
        Column(Modifier.padding(16.dp)) {
            RDropdown(
                items = items,
                selectedItem = selected,
                onItemSelected = { selected = it }
            )
        }
    }
}