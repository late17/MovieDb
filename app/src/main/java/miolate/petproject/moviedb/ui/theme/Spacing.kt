package miolate.petproject.moviedb.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Spacing(
    val default: Dp = 0.dp,
    val lineHeight: Dp = 1.dp,
    val extraTiny: Dp = 1.dp,
    val tiny: Dp = 2.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 32.dp,
)

data class FontSizes(
    val caption: TextUnit = 10.sp,
    val tiny: TextUnit = 11.sp,
    val small: TextUnit = 13.sp,
    val medium: TextUnit = 16.sp,
    val large: TextUnit = 20.sp,
    val extraLarge: TextUnit = 24.sp,

    val appBar: TextUnit = 28.sp,
)

val spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current
val LocalSpacing = compositionLocalOf { Spacing() }

val fontSizes: FontSizes
    @Composable
    @ReadOnlyComposable
    get() = LocalFontSizes.current
val LocalFontSizes = compositionLocalOf { FontSizes() }
