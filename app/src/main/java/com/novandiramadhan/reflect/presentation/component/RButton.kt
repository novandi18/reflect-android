package com.novandiramadhan.reflect.presentation.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.ui.theme.ReflectTheme

@Composable
fun RButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = CircleShape,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    disabledContainerColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
    disabledContentColor: Color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
    fullWidth: Boolean = false,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    isLoading: Boolean = false,
) {
    Button(
        onClick = onClick,
        modifier = if (fullWidth) modifier.fillMaxWidth() else modifier,
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor
        ),
        contentPadding = contentPadding,
        interactionSource = interactionSource,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = contentColor
                )
            }
            Text(
                text = text,
                style = textStyle,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RButtonPreview() {
    ReflectTheme {
        RButton(
            text = stringResource(R.string.btn_continue),
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RButtonDisabledPreview() {
    ReflectTheme {
        RButton(
            text = stringResource(R.string.btn_continue),
            onClick = { },
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RButtonLoadingPreview() {
    ReflectTheme {
        RButton(
            text = stringResource(R.string.btn_continue),
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            isLoading = true
        )
    }
}