package miolate.petproject.moviedb.data.models

import kotlinx.serialization.Serializable

@Serializable
data class DiscoverResponse(
    val page: Int,
    val results: List<Result>,
    val totalPages: Int,
    val totalResults: Int
)