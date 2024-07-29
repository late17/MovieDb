package miolate.petproject.moviedb.data.remote.base

import android.util.Log
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
import java.net.UnknownHostException

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
            Log.e("BaseKtorApi", "ClientRequestException: ${e.message}", e)
            DataResult.Failure(e = DataError.Network.CLIENT_REQUEST_ERROR, message = e.getError())
        } catch (e: ServerResponseException) {
            Log.e("BaseKtorApi", "ServerResponseException: ${e.message}", e)
            DataResult.Failure(e = DataError.Network.SERVER_RESPONSE_ERROR, message = e.getError())
        } catch (e: UnknownHostException) {
            Log.e("BaseKtorApi", "UnknownHostException: ${e.message}", e)
            DataResult.Failure(e = DataError.Network.NO_INTERNET)
        } catch (e: IOException) {
            Log.e("BaseKtorApi", "IOException: ${e.message}", e)
            DataResult.Failure(e = DataError.Network.NO_INTERNET)
        } catch (e: SerializationException) {
            Log.e("BaseKtorApi", "SerializationException: ${e.message}", e)
            DataResult.Failure(e = DataError.Network.SERIALIZATION)
        } catch (e: Exception) {
            Log.e("BaseKtorApi", "Exception: ${e.message}", e)
            DataResult.Failure(e = DataError.Network.UNKNOWN)
        }

    protected suspend inline fun ResponseException.getError() =
        try {
            response.body<ErrorResponse>().statusMessage
        } catch (e: Exception) {
            Log.e("BaseKtorApi", "getError Exception: ${e.message}", e)
            null
        }
}
