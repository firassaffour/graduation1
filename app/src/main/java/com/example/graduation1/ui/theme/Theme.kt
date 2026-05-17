package com.example.graduation1.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(216, 13, 16),
    secondary = Color(30, 2, 31),
    background = Color(23, 21, 21, 255),
    surface = Color(115, 112, 112, 255),
    onPrimary = Color.Black,
    onBackground = Color.White,
    tertiary = primaryRed
)

private val LightColorScheme = lightColorScheme(
    primary = Color(216, 13, 16),
    secondary = Color(30, 2, 31),
    background = Color(255, 255, 255),
    surface = Color(217, 217, 217),
    onPrimary = Color.Black,
    onBackground = Color.Black,
    tertiary = primaryRed

)

@Composable
fun Graduation1Theme(
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}