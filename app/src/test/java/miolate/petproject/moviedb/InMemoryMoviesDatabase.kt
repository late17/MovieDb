package miolate.petproject.moviedb

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import miolate.petproject.moviedb.data.local.MoviesDatabase
import miolate.petproject.moviedb.domain.model.IsFavorite
import miolate.petproject.moviedb.domain.model.Movie

class InMemoryMoviesDatabase : MoviesDatabase {

    private val movieList = mutableListOf<Movie>()

    override suspend fun getAll(): List<Movie> = movieList

    override suspend fun getAllFavorite(): Flow<List<Movie>> = flowOf(movieList.filter { it.isFavourite == IsFavorite.FAVORITE })

    override suspend fun getById(movieId: Int): Movie? = movieList.find { it.id == movieId }

    override suspend fun insert(movie: Movie) {
        movieList.removeIf { it.id == movie.id }
        movieList.add(movie)
    }

    override suspend fun insertAll(movies: List<Movie>) {
        movies.forEach { insert(it) }
    }

    override suspend fun delete(movie: Movie) {
        movieList.removeIf { it.id == movie.id }
    }

    override suspend fun deleteAll(movies: List<Movie>) {
        movies.forEach { delete(it) }
    }

    override suspend fun update(movie: Movie): Int {
        val index = movieList.indexOfFirst { it.id == movie.id }
        return if (index != -1) {
            movieList[index] = movie
            1
        } else {
            0
        }
    }
}
