package com.novandiramadhan.reflect.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@Composable
fun PasswordField(
    modifier: Modifier = Modifier,
    label: String = "Password",
    password: String,
    onPasswordChange: (String) -> Unit,
    isError: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    onDone: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(false) }
    var isPasswordValid by remember { mutableStateOf(true) }
    val interactionSource = remember { MutableInteractionSource() }

    val errorMessage = when {
        password.isEmpty() && !isPasswordValid -> stringResource(R.string.password_empty)
        !isPasswordValid -> stringResource(R.string.password_error)
        else -> ""
    }

    Column(modifier = modifier.fillMaxWidth()) {
        TextField(
            value = password,
            onValueChange = {
                onPasswordChange(it)
                isPasswordValid = it.isEmpty() || it.length >= 8
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
            isError = !isPasswordValid || isError,
            enabled = enabled,
            readOnly = readOnly,
            singleLine = true,
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onDone()
                }
            ),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible)
                            stringResource(R.string.hide_password)
                        else
                            stringResource(R.string.show_password),
                        tint = if (!isPasswordValid || isError)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            },
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

        if (!isPasswordValid || isError) {
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
private fun PasswordFieldPreview() {
    var password by remember { mutableStateOf("") }
    ReflectTheme {
        PasswordField(
            password = password,
            onPasswordChange = { password = it }
        )
    }
}