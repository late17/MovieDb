package miolate.petproject.moviedb.features.home

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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import miolate.petproject.moviedb.domain.model.Movie
import miolate.petproject.moviedb.ui.base.SpacerValue
import miolate.petproject.moviedb.ui.base.SpacerWeight
import miolate.petproject.moviedb.ui.base.collectAsEffect
import miolate.petproject.moviedb.ui.theme.fontSizes
import miolate.petproject.moviedb.ui.theme.spacing
import java.time.LocalDate

@Composable
fun HomeScreen(navController: NavController) {

    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.apply {
        actions.collectAsEffect { action ->
            when (action) {
                is HomeViewModel.HomeActions.ShowError -> {
                    //show error message
                }
            }
        }
    }

    UI(uiState, viewModel::onEvent)
}

@Composable
@Preview
fun HomeScreenPreview() {
    val homeState = getPreviewData()
    UI(uiState = homeState) {}
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UI(
    uiState: HomeState,
    onEvent: (HomeEvents) -> Unit = {}
) {
    val pullRefreshState = rememberPullRefreshState(refreshing = uiState.isLoading, onRefresh = {onEvent(HomeEvents.PullRefresh)})
    val gridState = rememberLazyGridState()
    val uiStateFlow = rememberUpdatedState(newValue = uiState)

    LaunchedEffect(gridState) {
        combine(
            snapshotFlow { gridState.firstVisibleItemIndex },
            snapshotFlow { uiStateFlow }
        ) { firstVisibleItemIndex, updatedUiState ->
            Pair(firstVisibleItemIndex, updatedUiState)
        }
            //Only launched when gridState triggers, but still
            //Should be refactored as I don't like this method (not adaptive)
            //I mean this line and number 6 in it: updatedUiState.value.movies.size - 6
            .filter { (firstVisibleItemIndex, updatedUiState) -> firstVisibleItemIndex >= updatedUiState.value.movies.size - 6 && !updatedUiState.value.endReached && !updatedUiState.value.isLoadingNewItems }
            .collect {
                onEvent(HomeEvents.LoadNextItems)
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .pullRefresh(pullRefreshState)
            .padding(horizontal = spacing.small, vertical = spacing.small)
    ) {
        LazyVerticalGrid(
            state = gridState,
            //adapted for Vertical screen only, for Horizontal Should Use Adaptive
            columns = GridCells.Fixed(2),
        ) {
            items(uiState.movies) { movie ->
                MovieView(movie = movie, onEvent)
            }
            item {
                if (uiState.isLoadingNewItems) {
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
            uiState.isLoading,
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
                Text(
                    text = movie.yearAndMonthUI,
                    maxLines = 1,
                )
            }
            SpacerValue(spacing.small)
            Row {
                Icon(
                    modifier = Modifier.clickable {
                        onEvent(HomeEvents.LikeMovie(movie.id))
                    },
                    painter = painterResource(id = R.drawable.baseline_favorite_24),
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


private fun getPreviewData(): HomeState {
    val movies = listOf(
        Movie(
            adult = true,
            backdropPath = "https://dummyimage.com/831x619",
            genreIds = listOf(62, 89, 14, 67, 90),
            id = 975218,
            originalLanguage = "pt",
            originalTitle = "Reverse-engineered holistic artificial intelligence",
            overview = "Traditional discuss natural wear. Eight business include nothing growth red. Participant current never action begin example situation become.",
            popularity = 77.052,
            posterPath = "https://placeimg.com/453/1/any",
            releaseDate = LocalDate.now(),
            yearAndMonthUI = "June 2024",
            title = "American serve magazine.",
            video = false,
            voteAverage = 8.6,
            voteCount = 377,
        ),
        Movie(
            adult = false,
            backdropPath = "https://dummyimage.com/680x80",
            genreIds = listOf(39, 81, 53, 89, 19),
            id = 545263,
            originalLanguage = "bs",
            originalTitle = "Balanced actuating moratorium",
            overview = "Walk half for. Draw coach store top might policy.\nDebate main population ok position what agency. Answer level rest boy them behind.",
            popularity = 104.471,
            posterPath = "https://placeimg.com/800/263/any",
            releaseDate = LocalDate.now(),
            yearAndMonthUI = "June 2024",
            title = "Safe medical.",
            video = false,
            voteAverage = 1.3,
            voteCount = 810
        ),
        Movie(
            adult = false,
            backdropPath = "https://www.lorempixel.com/667/119",
            genreIds = listOf(59, 69, 86, 39, 71),
            id = 458459,
            originalLanguage = "sw",
            originalTitle = "Virtual incremental policy",
            overview = "Hotel know relationship include do from history environment. Let sign wish part start major specific front. Part case show newspaper.",
            popularity = 114.655,
            posterPath = "https://dummyimage.com/301x190",
            releaseDate = LocalDate.now().minusMonths(2),
            yearAndMonthUI = "June 2024",
            title = "Accept.",
            video = true,
            voteAverage = 3.9,
            voteCount = 1354
        ),
        Movie(
            adult = true,
            backdropPath = "https://placekitten.com/282/600",
            genreIds = listOf(29, 15, 25, 98, 46),
            id = 891438,
            originalLanguage = "eu",
            originalTitle = "Quality-focused discrete help-desk",
            overview = "Store small blue growth shoulder reduce. Ok fill your change. Finally become good moment region case.\nFind bill learn act. Community about phone light crime any. Base my ready product red.",
            popularity = 8.702,
            posterPath = "https://placekitten.com/866/227",
            releaseDate = LocalDate.now().minusMonths(2),
            yearAndMonthUI = "June 2024",
            title = "Already from.",
            video = true,
            voteAverage = 2.7,
            voteCount = 540
        ),
        Movie(
            adult = true,
            backdropPath = "https://placeimg.com/862/523/any",
            genreIds = listOf(54, 7, 27, 2, 66),
            id = 730907,
            originalLanguage = "ar",
            originalTitle = "Customizable client-driven customer loyalty",
            overview = "Tonight amount not. Soldier value foreign of. Side often walk society deal just.\nThus ok institution question. Glass happen it soldier national rule. Instead message loss weight.",
            popularity = 69.414,
            posterPath = "https://placekitten.com/750/496",
            releaseDate = LocalDate.now().minusMonths(4),
            yearAndMonthUI = "June 2024",
            title = "Issue.",
            video = false,
            voteAverage = 1.8,
            voteCount = 1232
        ),
    )
    val homeState = HomeState(movies = movies)
    return homeState
}