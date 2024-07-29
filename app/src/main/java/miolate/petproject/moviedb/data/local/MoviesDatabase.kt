package miolate.petproject.moviedb.data.local

import kotlinx.coroutines.flow.Flow
import miolate.petproject.moviedb.domain.model.Movie

interface MoviesDatabase {

    suspend fun getAll(): List<Movie>

    suspend fun getAllFavorite(): Flow<List<Movie>>

    suspend fun getById(movieId: Int): Movie?

    suspend fun insert(movie: Movie)

    suspend fun insertAll(movies: List<Movie>)

    suspend fun delete(movie: Movie)

    suspend fun deleteAll(movies: List<Movie>)

    suspend fun update(movie: Movie): Int
}