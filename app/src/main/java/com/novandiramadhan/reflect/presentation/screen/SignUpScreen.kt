package com.novandiramadhan.reflect.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.presentation.component.EmailField
import com.novandiramadhan.reflect.presentation.component.PasswordField
import com.novandiramadhan.reflect.presentation.component.RButton
import com.novandiramadhan.reflect.presentation.component.RTextField
import com.novandiramadhan.reflect.presentation.component.auth.GoogleSignInButton
import com.novandiramadhan.reflect.presentation.state.SignUpUiState
import com.novandiramadhan.reflect.presentation.viewmodel.SignUpViewModel
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    back: () -> Unit = {},
    navigateToHome: () -> Unit = {}
) {
    var isGoogleAuth = false
    val uiState by viewModel.uiState.collectAsState()
    val fullName by viewModel.name.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState) {
        when (uiState) {
            is SignUpUiState.Success -> {
                if (isGoogleAuth) navigateToHome() else back()
                viewModel.clearState()
            }
            is SignUpUiState.Error -> {
                Toast.makeText(
                    context,
                    (uiState as SignUpUiState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(R.drawable.reflect),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .padding(8.dp)
        )

        Text(
            text = stringResource(R.string.register_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = stringResource(R.string.register_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        RTextField(
            value = fullName,
            onValueChange = viewModel::setName,
            label = stringResource(R.string.full_name),
            placeholder = stringResource(R.string.full_name),
            isError = fullName.isNotEmpty(),
            errorMessage = stringResource(R.string.name_empty)
        )

        EmailField(
            email = email,
            onEmailChange = viewModel::setEmail
        )

        PasswordField(
            password = password,
            onPasswordChange = viewModel::setPassword
        )

        Spacer(modifier = Modifier.height(8.dp))

        RButton(
            text = stringResource(R.string.register_title),
            onClick = {
                isGoogleAuth = false
                viewModel.register()
            },
            enabled = uiState !is SignUpUiState.Loading &&
                    fullName.isNotBlank() &&
                    email.isNotEmpty() &&
                    password.isNotEmpty(),
            fullWidth = true,
            modifier = Modifier.height(52.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.or),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        GoogleSignInButton(
            onClick = {
                isGoogleAuth = true
                viewModel.getTokenGoogleAuth()
            },
            enabled = uiState !is SignUpUiState.Loading
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(R.string.terms_and_conditions),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenPreview() {
    ReflectTheme {
        SignUpScreen()
    }
}