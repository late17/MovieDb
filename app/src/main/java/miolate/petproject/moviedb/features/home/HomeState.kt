package miolate.petproject.moviedb.features.home

import miolate.petproject.moviedb.domain.model.Movie

data class HomeState(
    val movies: List<Movie> = emptyList(),
    val page: Int = 1,
    val endReached: Boolean = false,
    val isLoadingNewItems: Boolean = false,
    val isLoading: Boolean = false,
)