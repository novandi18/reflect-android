package com.novandiramadhan.reflect.presentation.component.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@Composable
fun GoogleSignInButton(
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    val context = LocalContext.current

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier,
        enabled = enabled,
        shape = CircleShape,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = stringResource(R.string.continue_with_google),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview
@Composable
private fun GoogleSignInButtonPreview() {
    ReflectTheme {
        GoogleSignInButton(
            onClick = {},
        )
    }
}