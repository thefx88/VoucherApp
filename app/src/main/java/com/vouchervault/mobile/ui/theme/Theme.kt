package com.vouchervault.mobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Purple40,
    onPrimary = Color.White,
    secondary = PurpleGrey40,
    onSecondary = Color.White,
    tertiary = Pink40,
    onTertiary = Color.White,
    background = Color(0xFFF7F7F7),
    surface = Color.White,
    onSurface = Color(0xFF1C1B1F)
)

@Composable
fun VoucherVaultTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = LightColors // The design intentionally uses a bright palette inspired by Klarna

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
