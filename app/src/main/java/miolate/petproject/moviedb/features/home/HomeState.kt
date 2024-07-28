package miolate.petproject.moviedb.features.home

import miolate.petproject.moviedb.domain.model.Movie

data class HomeState(
    val movies: List<Movie> = emptyList(),
    val favouritesMovies: List<Movie> = emptyList(),
    val page: Int = 1,
    val endReached: Boolean = false,
    val isLoadingNewItems: Boolean = false,
    // This can be replaced with Screen State Sealed Class.
    val isLoading: Boolean = true,
) {

    fun insertMovie(updatedMovie: Movie): HomeState {
        val newList = movies.toMutableList()
        val indexOfFirst = newList.indexOfFirst { it.id == updatedMovie.id }
        newList[indexOfFirst] = updatedMovie
        return this.copy(
            movies = newList
        )
    }
}