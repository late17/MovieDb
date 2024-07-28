package miolate.petproject.moviedb.features.home

import miolate.petproject.moviedb.domain.model.Movie
import java.time.YearMonth

data class HomeState(
    val movies: List<Movie> = emptyList(),
//    val oldMovies: Map<YearMonth, List<Movie>> = emptyMap(),
    val page: Int = 1,
    val endReached: Boolean = false,
    val isLoadingNewItems: Boolean = false,
    val isLoading: Boolean = false,
)