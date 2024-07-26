package miolate.petproject.moviedb.domain

import miolate.petproject.moviedb.app.base.DataError
import miolate.petproject.moviedb.app.base.DataResult
import miolate.petproject.moviedb.domain.model.Movie

interface MoviesRepository {

    suspend fun getMovies(pageNumber: Int): DataResult<List<Movie>, DataError>
}