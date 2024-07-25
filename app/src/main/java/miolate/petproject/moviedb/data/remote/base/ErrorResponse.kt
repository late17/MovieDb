package miolate.petproject.moviedb.data.remote.base

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val statusCode: Int,
    val statusMessage: String,
    val success: Boolean
)
