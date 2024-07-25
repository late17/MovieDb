package miolate.petproject.moviedb.data.models

import kotlinx.serialization.Serializable

@Serializable
data class DiscoverResponse(
    val page: Int,
    val resultResponses: List<ResultResponse>,
    val totalPages: Int,
    val totalResults: Int
)