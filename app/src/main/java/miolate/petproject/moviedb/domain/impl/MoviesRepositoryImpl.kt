package miolate.petproject.moviedb.domain.impl

import kotlinx.coroutines.flow.Flow
import miolate.petproject.moviedb.app.base.DataError
import miolate.petproject.moviedb.app.base.DataResult
import miolate.petproject.moviedb.app.base.mapIfSuccessSus
import miolate.petproject.moviedb.data.local.MoviesDatabase
import miolate.petproject.moviedb.data.remote.RemoteDataSource
import miolate.petproject.moviedb.domain.MoviesRepository
import miolate.petproject.moviedb.domain.model.IsFavorite
import miolate.petproject.moviedb.domain.model.Movie
import miolate.petproject.moviedb.util.runOnDefault

class MoviesRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val moviesDatabase: MoviesDatabase,
) : MoviesRepository {

    override suspend fun getFavouritesMovies(): Flow<List<Movie>> = moviesDatabase.getAllFavorite()

    override suspend fun addToFavorites(movie: Movie) {
        moviesDatabase.insert(movie.copy(isFavourite = IsFavorite.FAVORITE))
    }

    override suspend fun removeFromFavorites(movie: Movie) {
        return moviesDatabase.delete(movie)
    }

    override suspend fun getMovies(pageNumber: Int): DataResult<List<Movie>, DataError> {
        return remoteDataSource.getMovies(pageNumber)
            .mapIfSuccessSus { it ->
                it.results.map { it.toMovie() }
            }
    }
}