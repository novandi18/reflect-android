package com.novandiramadhan.reflect.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@Composable
fun RTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    validateInput: ((String) -> Boolean)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    minLines: Int = 1,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    borderColor: Color = Color.Transparent,
    onDone: () -> Unit = {},
    onNext: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    var isFieldError by remember { mutableStateOf(isError) }
    var fieldErrorMessage by remember { mutableStateOf(errorMessage) }
    val interactionSource = remember { MutableInteractionSource() }

    if (validateInput != null && value.isNotEmpty()) {
        isFieldError = !validateInput(value)
        fieldErrorMessage = if (isFieldError) errorMessage else null
    }

    Column(modifier = modifier.fillMaxWidth()) {
        TextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                if (validateInput != null && it.isNotEmpty()) {
                    isFieldError = !validateInput(it)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 1.dp,
                    color = if (isFieldError) MaterialTheme.colorScheme.error else borderColor,
                    shape = MaterialTheme.shapes.small
                ),
            textStyle = MaterialTheme.typography.bodyMedium,
            label = label?.let {
                {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            placeholder = placeholder?.let { {
                Text(text = it, color = borderColor.copy(.6f))
            } },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isFieldError,
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                    onNext()
                },
                onDone = {
                    focusManager.clearFocus()
                    onDone()
                }
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

        if (isFieldError && !fieldErrorMessage.isNullOrEmpty()) {
            Text(
                text = fieldErrorMessage ?: "Unknown error",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RTextFieldPreview() {
    ReflectTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            var text by remember { mutableStateOf("") }
            RTextField(
                value = text,
                onValueChange = { text = it },
                label = "Username",
                placeholder = "Enter your username",
                isError = true,
                errorMessage = "Username is required"
            )
        }
    }
}