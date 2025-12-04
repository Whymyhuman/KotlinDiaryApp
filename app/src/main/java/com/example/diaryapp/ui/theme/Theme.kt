package com.example.diaryapp.ui.theme

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

private val LightColorScheme = lightColorScheme(
    primary = DiaryBlue,
    onPrimary = WarmWhite,
    primaryContainer = DiaryBlueVariant,
    onPrimaryContainer = WarmWhite,
    secondary = DiaryPink,
    onSecondary = WarmWhite,
    secondaryContainer = SoftCream,
    onSecondaryContainer = DeepCharcoal,
    background = SoftCream,
    onBackground = DeepCharcoal,
    surface = WarmWhite,
    onSurface = DeepCharcoal,
    surfaceVariant = SoftCream,
    onSurfaceVariant = DeepCharcoal,
    outline = SoftGray
)

private val DarkColorScheme = darkColorScheme(
    primary = DiaryBlueDark,
    onPrimary = WarmWhite,
    primaryContainer = DiaryBlueVariantDark,
    onPrimaryContainer = WarmWhite,
    secondary = DiaryPinkDark,
    onSecondary = WarmWhite,
    secondaryContainer = SoftCreamDark,
    onSecondaryContainer = DeepCharcoalDark,
    background = DeepCharcoalDark,
    onBackground = WarmWhite,
    surface = DeepCharcoal,
    onSurface = WarmWhite,
    surfaceVariant = DeepCharcoal,
    onSurfaceVariant = WarmWhite,
    outline = SoftGray
)

@Composable
fun DiaryAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

