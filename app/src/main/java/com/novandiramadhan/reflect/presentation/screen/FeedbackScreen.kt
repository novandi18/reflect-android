package com.novandiramadhan.reflect.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.presentation.component.RTextField
import com.novandiramadhan.reflect.ui.theme.ReflectTheme
import com.novandiramadhan.reflect.util.sendFeedbackEmail

@Composable
fun FeedbackScreen() {
    val context = LocalContext.current
    var feedback by remember { mutableStateOf("") }
    var sent by remember { mutableStateOf(false) }

    val email = stringResource(R.string.email_developer)
    val subject = stringResource(R.string.feedback_subject)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.feedback_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        RTextField(
            value = feedback,
            onValueChange = { feedback = it },
            placeholder = stringResource(R.string.feedback_hint),
            maxLines = 6,
            minLines = 6,
            singleLine = false,
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                sendFeedbackEmail(context, email, subject, feedback)
                sent = true
                feedback = ""
            },
            enabled = feedback.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.feedback_send))
        }
        if (sent) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.feedback_sent),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FeedbackScreenPreview() {
    ReflectTheme {
        FeedbackScreen()
    }
}