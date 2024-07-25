package miolate.petproject.moviedb.data.remote.base

import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.reflect.typeInfo
import kotlinx.serialization.SerializationException
import miolate.petproject.moviedb.app.base.DataError
import miolate.petproject.moviedb.app.base.DataResult
import java.io.IOException

open class BaseKtorApi {

    protected suspend inline fun <reified T> HttpResponse.safeResult(): DataResult<T, DataError> =
        try {
            when (this.status.value) {
                in 200..299 -> DataResult.Success(call.bodyNullable(typeInfo<T>()) as T)
                in 400..499 -> throw ClientRequestException(this, this.status.description)
                in 500..599 -> throw ServerResponseException(this, this.status.description)
                else -> {
                    throw Exception()
                }
            }
        } catch (e: ClientRequestException) {
            DataResult.Failure(e = DataError.Network.CLIENT_REQUEST_ERROR, message = e.getError())
        } catch (e: ServerResponseException) {
            DataResult.Failure(e = DataError.Network.SERVER_RESPONSE_ERROR, message = e.getError())
        } catch (e: IOException) {
            DataResult.Failure(e = DataError.Network.NO_INTERNET)
        } catch (e: SerializationException) {
            DataResult.Failure(e = DataError.Network.SERIALIZATION)
        } catch (e: Exception) {
            DataResult.Failure(e = DataError.Network.UNKNOWN)
        }

    protected suspend inline fun ResponseException.getError() =
        try {
            response.body<ErrorResponse>().statusMessage
        } catch (e: Exception) {
            null
        }
}
