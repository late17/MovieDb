package miolate.petproject.moviedb.features.home.data

import androidx.compose.runtime.Immutable
import miolate.petproject.moviedb.domain.model.Movie

@Immutable
data class HomeState(
    //
    val movies: List<Movie> = emptyList(),
    val favouritesMovies: List<Movie> = emptyList(),
    val cashMovies: List<Movie> = emptyList(),
    val page: Int = 1,
    val endReached: Boolean = false,
    val isLoadingNewItems: Boolean = false,
    // This can be replaced with Screen State Sealed Class.
//    val isLoading: Boolean = true,
    val status: ScreenStatus = ScreenStatus.Loading,
)