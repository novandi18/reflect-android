package com.novandiramadhan.reflect.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.presentation.component.RDropdown
import com.novandiramadhan.reflect.presentation.component.RTextField
import com.novandiramadhan.reflect.ui.theme.ReflectTheme
import com.novandiramadhan.reflect.util.sendBugReportEmail

@Composable
fun ReportBugScreen() {
    val context = LocalContext.current
    val bugCategories = stringArrayResource(id = R.array.bug_categories).toList()
    var selectedCategory by remember { mutableStateOf<String?>(bugCategories.firstOrNull()) }
    var description by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }
    var showSentToast by remember { mutableStateOf(false) }

    val email = stringResource(id = R.string.email_developer)
    val subject = stringResource(id = R.string.report_subject)
    val opening = stringResource(id = R.string.report_email_body_opening)
    val closing = stringResource(id = R.string.report_email_body_closing)

    if (showSentToast) {
        Toast.makeText(context, stringResource(R.string.report_sent), Toast.LENGTH_LONG).show()
        showSentToast = false
    }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = stringResource(R.string.report_bug_description),
            style = MaterialTheme.typography.bodyMedium
        )
        RDropdown(
            items = bugCategories,
            selectedItem = selectedCategory,
            onItemSelected = { selectedCategory = it },
            label = stringResource(R.string.bug_category_label),
            modifier = Modifier.width(220.dp)
        )
        RTextField(
            value = description,
            onValueChange = { description = it },
            label = stringResource(R.string.bug_description_label),
            placeholder = "",
            singleLine = false,
            minLines = 4,
            maxLines = 8,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                isSending = true
                sendBugReportEmail(
                    context = context,
                    to = email,
                    subject = subject,
                    category = selectedCategory ?: "",
                    description = description,
                    opening = opening,
                    closing = closing
                )
                isSending = false
                showSentToast = true
                description = ""
                selectedCategory = bugCategories.firstOrNull()
            },
            enabled = !isSending && description.isNotBlank() && selectedCategory != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.send_report))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReportBugScreenPreview() {
    ReflectTheme {
        ReportBugScreen()
    }
}