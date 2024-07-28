package miolate.petproject.moviedb.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import miolate.petproject.moviedb.data.local.dao.MovieDao
import miolate.petproject.moviedb.domain.model.Movie
import miolate.petproject.moviedb.util.runOnDefault

class MoviesDatabase(private val movieDao: MovieDao) : BaseRepository() {

    suspend fun getAll(): List<Movie> {
        return runOnDefault {
            movieDao.getAll().map { it.toMovie() }
        }
    }

    fun getAllFavorite(): Flow<List<Movie>> {
        return movieDao
            .getAllFlow()
            .map { list -> list
                .filter { it.isFavorite }
                .map { it.toMovie() }
            }
    }

    suspend fun getById(movieId: Int): Movie? {
        return runOnDefault {
            movieDao.getById(movieId)?.toMovie()
        }
    }

    suspend fun insert(movie: Movie) {
        runOnDefault {
            movieDao.insert(movie.toMovieEntity())
        }
    }

    suspend fun delete(movie: Movie) {
        runOnDefault {
            movieDao.delete(movie.toMovieEntity())
        }
    }

    suspend fun update(movie: Movie): Int {
        return runOnDefault {
            movieDao.update(movie.toMovieEntity())
        }
    }
}