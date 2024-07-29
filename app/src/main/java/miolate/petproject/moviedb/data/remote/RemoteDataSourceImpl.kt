package miolate.petproject.moviedb.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import miolate.petproject.moviedb.app.base.DataError
import miolate.petproject.moviedb.app.base.DataResult
import miolate.petproject.moviedb.data.models.DiscoverResponse
import miolate.petproject.moviedb.data.remote.base.BaseKtorApi

class RemoteDataSourceImpl(private val client: HttpClient) : RemoteDataSource, BaseKtorApi() {

    override suspend fun getMovies(pageNumber: Int): DataResult<DiscoverResponse, DataError> {
        // Turns out the safe Result cannot catch the UnknownHostException (no internet)
        // Because the exception is thrown before getting into safe result
        // Due to lack of time, I'll leave it like this
        // But most likely I would create the wrapper for RemoteDataSource, which
        // Will inherit BaseKtor API and will take care of SafeResult functionality
        return try {
            client.get {
                url("discover/movie")
                parameter("page", pageNumber)
                parameter("sort_by", "primary_release_date.des")
                parameter("vote_count.gte", 100)
                parameter("vote_average.gte", 7)
                parameter("language", "en-US")
            }.safeResult()
        } catch (e: Exception) {
            DataResult.Failure(e = DataError.Network.NO_INTERNET)
        }
    }
}