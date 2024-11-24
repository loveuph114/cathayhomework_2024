package cc.reece.cathayhomework_2024.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = lightColorScheme(
    primary = DarkGreen,
    secondary = DarkYellow,
    tertiary = DarkBlue,
    background = Dark,
    surface = Dark,
    primaryContainer = Dark,
    onPrimary = Light,
    onSecondary = Light,
    onTertiary = Light,
    onBackground = Light,
    onSurface = Light,
)

private val LightColorScheme = darkColorScheme(
    primary = Green,
    secondary = Yellow,
    tertiary = Blue,
    background = Light,
    surface = Light,
    primaryContainer = Light,
    onPrimary = Dark,
    onSecondary = Dark,
    onTertiary = Dark,
    onBackground = Dark,
    onSurface = Dark,
)

@Composable
fun CathayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}