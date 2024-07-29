package miolate.petproject.moviedb.data.models

import kotlinx.serialization.Serializable
import miolate.petproject.moviedb.domain.model.IsFavorite
import miolate.petproject.moviedb.domain.model.Movie
import miolate.petproject.moviedb.util.releaseDateToYearAndMonth
import miolate.petproject.moviedb.util.roundWithDecimalsToSave
import miolate.petproject.moviedb.util.standardTimeToLocalDate
import miolate.petproject.moviedb.util.standardTimeToYearAndMonth

@Serializable
data class ResultResponse(
    val adult: Boolean,
    val backdropPath: String?,
    val genreIds: List<Int>,
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
) {
    fun toMovie(): Movie = Movie(
        adult,
        backdropPath,
        genreIds,
        id,
        originalLanguage,
        originalTitle,
        overview,
        popularity,
        posterPath,
        releaseDate.standardTimeToLocalDate(),
        yearAndMonth = releaseDate.standardTimeToYearAndMonth(),
        title,
        video,
        (voteAverage / 2.0).roundWithDecimalsToSave(1),
        voteCount,
        isFavourite = IsFavorite.NOT_FAVORITE,
        isCashed = false
    )
}