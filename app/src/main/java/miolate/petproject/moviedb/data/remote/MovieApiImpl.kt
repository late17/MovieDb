package miolate.petproject.moviedb.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import miolate.petproject.moviedb.app.base.DataError
import miolate.petproject.moviedb.app.base.DataResult
import miolate.petproject.moviedb.data.models.DiscoverResponse
import miolate.petproject.moviedb.data.remote.base.BaseKtorApi

class MovieApiImpl(private val client: HttpClient) : MovieApi, BaseKtorApi() {

    override suspend fun getMovies(): DataResult<DiscoverResponse, DataError> =
        client.get {
            url("discover/movie")
        }.safeResult()
}