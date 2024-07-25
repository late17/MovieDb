package miolate.petproject.moviedb.data.remote.base

import kotlinx.serialization.Serializable

//Default Movie DB Error Response Data Class
@Serializable
data class ErrorResponse(
    val statusCode: Int,
    val statusMessage: String,
    val success: Boolean
)
