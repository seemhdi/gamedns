package com.gamedns.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = GamingGreen,
    onPrimary = LightBackground,
    primaryContainer = GamingGreenLight,
    onPrimaryContainer = LightOnBackground,
    
    secondary = GamingBlue,
    onSecondary = LightBackground,
    secondaryContainer = GamingBlueLight,
    onSecondaryContainer = LightOnBackground,
    
    tertiary = GamingPurple,
    onTertiary = LightBackground,
    tertiaryContainer = GamingPurpleLight,
    onTertiaryContainer = LightOnBackground,
    
    error = ErrorRed,
    onError = LightBackground,
    
    background = LightBackground,
    onBackground = LightOnBackground,
    
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurface,
    
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0)
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkGamingGreen,
    onPrimary = DarkBackground,
    primaryContainer = GamingGreen,
    onPrimaryContainer = DarkOnBackground,
    
    secondary = DarkGamingBlue,
    onSecondary = DarkBackground,
    secondaryContainer = GamingBlue,
    onSecondaryContainer = DarkOnBackground,
    
    tertiary = DarkGamingPurple,
    onTertiary = DarkBackground,
    tertiaryContainer = GamingPurple,
    onTertiaryContainer = DarkOnBackground,
    
    error = ErrorRed,
    onError = DarkBackground,
    
    background = DarkBackground,
    onBackground = DarkOnBackground,
    
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurface,
    
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F)
)

@Composable
fun GameDNSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
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
