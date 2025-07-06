package com.novandiramadhan.reflect.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.ui.theme.ReflectTheme
import com.novandiramadhan.reflect.util.FieldValidation

@Composable
fun EmailField(
    modifier: Modifier = Modifier,
    label: String = "Email",
    email: String,
    onEmailChange: (String) -> Unit,
    isError: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
) {
    val focusManager = LocalFocusManager.current
    var isEmailValid by remember { mutableStateOf(true) }
    val interactionSource = remember { MutableInteractionSource() }

    val errorMessage = when {
        email.isEmpty() && !isEmailValid -> stringResource(R.string.email_empty)
        !isEmailValid -> stringResource(R.string.email_error)
        else -> ""
    }

    Column(modifier = modifier.fillMaxWidth()) {
        TextField(
            value = email,
            onValueChange = {
                onEmailChange(it)
                isEmailValid = FieldValidation.isValidEmail(it) || it.isEmpty()
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
            textStyle = MaterialTheme.typography.bodyMedium,
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            isError = !isEmailValid || isError,
            enabled = enabled,
            readOnly = readOnly,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.clearFocus() }
            ),
            interactionSource = interactionSource,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                errorContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                errorLabelColor = MaterialTheme.colorScheme.error,
                errorCursorColor = MaterialTheme.colorScheme.error,
                errorTrailingIconColor = MaterialTheme.colorScheme.error,
            )
        )

        if (!isEmailValid || isError) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Preview
@Composable
private fun EmailFieldPreview() {
    var email by remember { mutableStateOf("") }
    ReflectTheme {
        EmailField(
            email = email,
            onEmailChange = { email = it },
            isError = false,
            enabled = true,
            readOnly = false
        )
    }
}