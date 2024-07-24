package miolate.petproject.moviedb.ui.base

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.SpacerWeight(weight: Float = 1f) {
    Spacer(
        modifier = Modifier
            .weight(weight)
    )
}

@Composable
fun ColumnScope.SpacerWeight(weight: Float = 1f) {
    Spacer(
        modifier = Modifier
            .weight(weight)
    )
}

@Composable
fun SpacerValue(height: Dp = 0.dp, width: Dp = 0.dp) {
    Spacer(
        modifier = Modifier
            .height(height)
            .width(width)
    )
}

@Composable
fun ColumnScope.SpacerValue(height: Dp = 0.dp) {
    Spacer(
        modifier = Modifier
            .height(height)
    )
}

@Composable
fun RowScope.SpacerValue(width: Dp = 0.dp) {
    Spacer(
        modifier = Modifier
            .width(width)
    )
}