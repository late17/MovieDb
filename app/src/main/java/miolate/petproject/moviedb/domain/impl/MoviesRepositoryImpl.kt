package miolate.petproject.moviedb.domain.impl

import miolate.petproject.moviedb.app.base.DataError
import miolate.petproject.moviedb.app.base.DataResult
import miolate.petproject.moviedb.app.base.mapIfSuccess
import miolate.petproject.moviedb.data.remote.RemoteDataSource
import miolate.petproject.moviedb.domain.MoviesRepository
import miolate.petproject.moviedb.domain.model.Movie

class MoviesRepositoryImpl(private val remoteDataSource: RemoteDataSource) : MoviesRepository {

    override suspend fun getMovies(pageNumber: Int): DataResult<List<Movie>, DataError> {
        return remoteDataSource.getMovies(pageNumber).mapIfSuccess { it -> it.results.map { it.toMovie() } }
    }
}