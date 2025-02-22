package miolate.petproject.moviedb.data.remote

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import miolate.petproject.moviedb.BuildConfig

const val THE_MOVIE_DB_URL = "https://api.themoviedb.org/3/"
const val THE_MOVIE_DB_IMAGE_URL = "https://image.tmdb.org/t/p/original/"
const val THE_MOVIE_DB_MOVIE_URL = "https://www.themoviedb.org/movie/"

object HttpClient {

    @OptIn(ExperimentalSerializationApi::class)
    fun getHttpClient(): HttpClient = HttpClient(Android) {

        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            url {
                url(THE_MOVIE_DB_URL)
                parameters.append("api_key", BuildConfig.API_KEY_MOVIE_DB)
            }
        }

        install(ContentNegotiation) {
            json(Json {
                this.namingStrategy = JsonNamingStrategy.SnakeCase
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
            engine {
                connectTimeout = 60_000
            }
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v("Logger Ktor =>", message)
                }

            }
            level = LogLevel.ALL
        }
    }
}