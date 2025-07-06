package com.novandiramadhan.reflect.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Green,
    onPrimary = White,
    background = Black,
    onBackground = White,
    surface = DarkBlack,
    onSurface = White,
    secondary = Green,
    onSecondary = White,
    tertiary = LightGreen,
    onTertiary = Black,
    surfaceVariant = LightGrey,
    onSurfaceVariant = Black,
)

private val LightColorScheme = lightColorScheme(
    primary = Green,
    onPrimary = White,
    background = LightGrey,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    secondary = Green,
    onSecondary = White,
    tertiary = LightGreen,
    onTertiary = Black,
    surfaceVariant = LightGrey,
    onSurfaceVariant = Black,
)

@Composable
fun ReflectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}