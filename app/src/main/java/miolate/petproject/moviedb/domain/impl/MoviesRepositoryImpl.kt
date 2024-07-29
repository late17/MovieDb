package miolate.petproject.moviedb.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import miolate.petproject.moviedb.app.base.DataError
import miolate.petproject.moviedb.app.base.DataResult
import miolate.petproject.moviedb.app.base.mapIfSuccessSus
import miolate.petproject.moviedb.data.local.MoviesDatabase
import miolate.petproject.moviedb.data.remote.RemoteDataSource
import miolate.petproject.moviedb.domain.MoviesRepository
import miolate.petproject.moviedb.domain.model.IsFavorite
import miolate.petproject.moviedb.domain.model.Movie

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

    override suspend fun getCashMovies(): List<Movie> {
        return moviesDatabase.getAll()
    }

    override suspend fun getMovies(pageNumber: Int): DataResult<List<Movie>, DataError> {
        return remoteDataSource.getMovies(pageNumber)
            .mapIfSuccessSus { it ->
                it.results
                    .map { it.toMovie() }
                    .also { if (pageNumber == 1) it.cashMovies() }
            }
    }

    private suspend fun List<Movie>.cashMovies() {
        supervisorScope {
            launch {
                updateDataBaseCash()
            }
        }
    }

    //
    private suspend fun List<Movie>.updateDataBaseCash() {
        val allMovies = moviesDatabase.getAll()
        val moviesToRemove = moviesDatabase.getAll()
            .filter { it.isCashed }
            .filter { it.isFavourite == IsFavorite.NOT_FAVORITE }
            .filter { m -> this.any { mLocal -> mLocal.id != m.id } }

        val toUpdate = mutableListOf<Int>()
        val toInsert = mutableListOf<Movie>()

        this.forEach { m ->
            if (allMovies.any { mLocal -> mLocal.id == m.id })
                toUpdate.add(m.id)
            else
                toInsert.add(m)
        }
        moviesDatabase.deleteAll(moviesToRemove)
        moviesDatabase.insertAll(toInsert.map { it.copy(isCashed = true) })
        toUpdate.forEach { it ->
            moviesDatabase.getById(it)?.copy(isCashed = true)?.let {
                moviesDatabase.insert(it)
            }
        }
    }
}