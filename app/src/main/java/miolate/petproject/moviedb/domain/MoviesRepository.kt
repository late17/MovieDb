package miolate.petproject.moviedb.domain

import kotlinx.coroutines.flow.Flow
import miolate.petproject.moviedb.app.base.DataError
import miolate.petproject.moviedb.app.base.DataResult
import miolate.petproject.moviedb.domain.model.Movie

interface MoviesRepository {

    suspend fun getMovies(pageNumber: Int): DataResult<List<Movie>, DataError>

    suspend fun getFavouritesMovies(): Flow<List<Movie>>

    suspend fun addToFavorites(movie: Movie)

    suspend fun removeFromFavorites(movie: Movie)
}