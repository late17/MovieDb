package miolate.petproject.moviedb.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import miolate.petproject.moviedb.ui.theme.spacing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(
    count: Int,
    pagerState: PagerState,
    tabsNames: List<String>,
    modifier: Modifier,
) {
    val scope = rememberCoroutineScope()
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            repeat(count) { index ->
                Column(modifier = Modifier
                    .clickable {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                    .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = tabsNames[index].uppercase(),
                        color = if (index == pagerState.currentPage)
                            MaterialTheme.colors.onPrimary
                        else
                            MaterialTheme.colors.onPrimary.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(
                            bottom = spacing.extraSmall,
                            top = spacing.extraSmall
                        )
                    )
                }
            }
        }
        LinearPagerIndicator(pageCount = count, currentPage = pagerState.currentPage)
    }
}

@Composable
fun LinearPagerIndicator(
    pageCount: Int,
    currentPage: Int,
    currentLineColor: Color = MaterialTheme.colors.onPrimary,
    lineColor: Color = MaterialTheme.colors.onPrimary.copy(alpha = 0.6f),
    lineHeight: Dp = 3.dp,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(lineHeight)
    ) {
        var width by remember { mutableStateOf(0.dp) }
        val localDensity = LocalDensity.current
        Box(modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { width = it.width.dp / localDensity.density / pageCount }
            .background(lineColor)
        )
        val dpOffset by animateDpAsState(targetValue = width * currentPage, label = "")
        Box(
            modifier = Modifier
                .width(width)
                .fillMaxHeight()
                .absoluteOffset { IntOffset((dpOffset * this.density).value.toInt(), 0) }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(currentLineColor)
            )
        }
    }
}
