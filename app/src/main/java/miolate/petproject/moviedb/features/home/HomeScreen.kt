package miolate.petproject.moviedb.features.home

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import miolate.petproject.moviedb.R
import miolate.petproject.moviedb.data.remote.THE_MOVIE_DB_IMAGE_URL
import miolate.petproject.moviedb.domain.model.IsFavorite
import miolate.petproject.moviedb.domain.model.Movie
import miolate.petproject.moviedb.features.home.data.HomeEvents
import miolate.petproject.moviedb.features.home.data.HomeState
import miolate.petproject.moviedb.features.home.data.ScreenStatus
import miolate.petproject.moviedb.ui.base.SpacerValue
import miolate.petproject.moviedb.ui.base.SpacerWeight
import miolate.petproject.moviedb.ui.base.collectAsEffect
import miolate.petproject.moviedb.ui.components.Tabs
import miolate.petproject.moviedb.ui.theme.fontSizes
import miolate.petproject.moviedb.ui.theme.spacing
import miolate.petproject.moviedb.ui.toast.showToast
import miolate.petproject.moviedb.util.shareLink
import miolate.petproject.moviedb.util.toUI
import java.time.YearMonth

@Composable
fun HomeScreen(navController: NavController) {

    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val groupedMovies by viewModel.groupedMovies.collectAsStateWithLifecycle()
    val context = LocalContext.current

    viewModel.apply {
        actions.collectAsEffect { action ->
            when (action) {
                is HomeViewModel.HomeActions.ShowToast -> {
                    context.showToast(action.error)
                }

                is HomeViewModel.HomeActions.ShareMovie -> {
                    context.shareLink(action.link)
                }
            }
        }
    }

    UI(uiState, groupedMovies, viewModel::onEvent)
}

@Composable
@Preview
fun HomeScreenPreview() {
    val homeState = getPreviewData()
    UI(state = homeState, emptyMap()) {}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UI(
    state: HomeState,
    groupedMovies: Map<YearMonth, List<Movie>>,
    onEvent: (HomeEvents) -> Unit = {}
) {
    val tabs =
        listOf(stringResource(id = R.string.discover), stringResource(id = R.string.favorites))
    val pagerState = rememberPagerState(0, 0F) { tabs.size }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = spacing.small, vertical = spacing.small)
    ) {
        Tabs(
            tabs.size,
            pagerState,
            tabs,
            Modifier
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
        ) { page ->
            when (page) {
                0 -> {
                    DiscoverPage(state, groupedMovies, onEvent)
                }

                1 -> {
                    FavoritePage(state, onEvent)
                }
            }
        }
    }
}

@Composable
fun FavoritePage(state: HomeState, onEvent: (HomeEvents) -> Unit) {
    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        state = gridState,
        //adapted for Vertical screen only, for Horizontal Should Use Adaptive
        columns = GridCells.Fixed(2),
    ) {
        items(state.favouritesMovies) { movie ->
            MovieView(movie = movie, onEvent)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DiscoverPage(
    state: HomeState,
    groupedMovies: Map<YearMonth, List<Movie>>,
    onEvent: (HomeEvents) -> Unit
) {

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.status == ScreenStatus.Loading,
        onRefresh = { onEvent(HomeEvents.PullRefresh) }
    )
    val gridState = rememberLazyGridState()

    LaunchedEffect(gridState) {
        combine(
            snapshotFlow { gridState.firstVisibleItemIndex },
            snapshotFlow { groupedMovies }
        ) { firstVisibleItemIndex, updatedUiState ->
            Pair(firstVisibleItemIndex, updatedUiState)
        }
            // Only launched when gridState triggers, but still
            // Should be refactored as I don't like this method (not adaptive)
            //
            .filter { (firstVisibleItemIndex, movies) -> firstVisibleItemIndex >= movies.values.size + movies.keys.size }
            .collect {
                onEvent(HomeEvents.LoadNextItems)
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        LazyVerticalGrid(
            state = gridState,
            //adapted for Vertical screen only, for Horizontal Should Use Adaptive
            columns = GridCells.Fixed(2),
        ) {
            groupedMovies.forEach { list ->
                item(key = list.key, span = { GridItemSpan(2) }) {
                    Text(
                        modifier = Modifier
                            .padding(spacing.small)
                            .padding(top = spacing.small),
                        text = list.key.toUI(),
                        fontSize = fontSizes.large
                    )
                }
                items(list.value, key = { it.id }) { movie ->
                    MovieView(movie = movie, onEvent)
                }
            }
            item {
                if (state.isLoadingNewItems && state.status != ScreenStatus.Loading) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        PullRefreshIndicator(
            state.status == ScreenStatus.Loading,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun MovieView(movie: Movie, onEvent: (HomeEvents) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .padding(spacing.small)
    ) {
        Column(modifier = Modifier.padding(spacing.extraSmall)) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(THE_MOVIE_DB_IMAGE_URL + movie.posterPath)
                    .crossfade(true)
                    .build(),
                // Used some default placeholder
                // I don't use SubcomposeAsyncImage due to worse performance
                placeholder = painterResource(R.drawable.baseline_image_24),
                contentDescription = "",
            )
            Text(
                text = movie.title,
                fontSize = fontSizes.medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            SpacerValue(spacing.small)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_star_24),
                    contentDescription = ""
                )
                SpacerValue(spacing.extraSmall)
                Text(text = "${movie.voteAverage}")
                SpacerWeight()
            }
            SpacerValue(spacing.small)
            Row {
                Icon(
                    modifier = Modifier.clickable {
                        if (movie.isFavourite == IsFavorite.FAVORITE)
                            onEvent(HomeEvents.RemoveFromFavorite(movie.id))
                        else
                            onEvent(HomeEvents.AddToFavorite(movie.id))
                    },
                    painter = painterResource(id = movie.isFavourite.resIconId),
                    contentDescription = ""
                )
                SpacerWeight()
                Icon(
                    modifier = Modifier.clickable {
                        onEvent(HomeEvents.ShareMovie(movie.id))
                    },
                    painter = painterResource(id = R.drawable.baseline_share_24),
                    contentDescription = ""
                )
            }
        }
    }
}
