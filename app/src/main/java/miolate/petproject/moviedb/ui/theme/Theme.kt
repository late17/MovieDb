package miolate.petproject.moviedb.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import miolate.librarium.ui_base.theme.FontSizes
import miolate.librarium.ui_base.theme.LocalFontSizes
import miolate.librarium.ui_base.theme.LocalSpacing
import miolate.librarium.ui_base.theme.Spacing

private val DarkColorScheme = darkColors(
    primary = Indigo,
    primaryVariant = Indigo,
    onPrimary = White,

    secondary = Green,
    secondaryVariant = Green,
    onSecondary = White,

    surface = PrimaryBlack,
    onSurface = White,

    background = PrimaryBlack,
    onBackground = White,

    error = Red,
    onError = White,
)

private val LightColorScheme = lightColors(
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)


@Composable
fun MovieDbTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = when {
        darkTheme -> DarkColorScheme
        else -> DarkColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    val systemUiController = rememberSystemUiController()

    LaunchedEffect(systemUiController) {
        systemUiController.setStatusBarColor(
            color = PrimaryBlack,
            darkIcons = false
        )
        systemUiController.setNavigationBarColor(
            color = SecondaryBlack,
            darkIcons = false
        )
    }

    CompositionLocalProvider(
        LocalSpacing provides Spacing(),
        LocalFontSizes provides FontSizes()
    ) {
        MaterialTheme(
            colors = colors,
            typography = Typography,
            content = content
        )
    }
}