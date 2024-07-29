package miolate.petproject.moviedb.data.remote

import miolate.petproject.moviedb.app.base.DataError
import miolate.petproject.moviedb.app.base.DataResult
import miolate.petproject.moviedb.data.models.DiscoverResponse

interface RemoteDataSource {

    suspend fun getMovies(pageNumber: Int): DataResult<DiscoverResponse, DataError>
}