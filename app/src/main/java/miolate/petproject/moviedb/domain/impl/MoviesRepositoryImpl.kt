package miolate.petproject.moviedb.domain.impl

import miolate.petproject.moviedb.data.remote.MovieApi
import miolate.petproject.moviedb.domain.MoviesRepository

class MoviesRepositoryImpl(private val movieApiImpl: MovieApi) : MoviesRepository{

    override suspend fun getMovies() {
        val movies = movieApiImpl.getMovies()
        println(movies)
    }
}