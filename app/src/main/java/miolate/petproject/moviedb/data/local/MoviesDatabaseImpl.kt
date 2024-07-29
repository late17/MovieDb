package miolate.petproject.moviedb.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import miolate.petproject.moviedb.data.local.dao.MovieDao
import miolate.petproject.moviedb.domain.model.Movie
import miolate.petproject.moviedb.util.runOnDefault

class MoviesDatabaseImpl(private val movieDao: MovieDao) : BaseRepository(), MoviesDatabase {

    override suspend fun getAll(): List<Movie> {
        return runOnDefault {
            movieDao.getAll().map { it.toMovie() }
        }
    }

    override suspend fun getAllFavorite(): Flow<List<Movie>> {
        return runOnDefault {
            movieDao
                .getAllFlow()
                .map { list ->
                    list
                        .filter { it.isFavorite }
                        .map { it.toMovie() }
                }
        }
    }

    override suspend fun getById(movieId: Int): Movie? {
        return runOnDefault {
            movieDao.getById(movieId)?.toMovie()
        }
    }

    override suspend fun insert(movie: Movie) {
        runOnDefault {
            movieDao.insert(movie.toMovieEntity())
        }
    }

    override suspend fun insertAll(movies: List<Movie>) {
        runOnDefault {
            movieDao.insertAll(movies.map { it.toMovieEntity() })
        }
    }

    override suspend fun delete(movie: Movie) {
        runOnDefault {
            movieDao.delete(movie.toMovieEntity())
        }
    }

    override suspend fun deleteAll(movies: List<Movie>) {
        runOnDefault {
            movieDao.deleteAll(movies.map { it.toMovieEntity() })
        }
    }

    override suspend fun update(movie: Movie): Int {
        return runOnDefault {
            movieDao.update(movie.toMovieEntity())
        }
    }
}