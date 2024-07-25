package miolate.petproject.moviedb.domain

interface MoviesRepository {

    suspend fun getMovies()
}