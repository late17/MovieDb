package miolate.petproject.moviedb.domain.model

import java.time.LocalDate
import java.time.YearMonth

data class Movie(
    val adult: Boolean,
    val backdropPath: String?,
    val genreIds: List<Int>,
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: LocalDate,
    val yearAndMonth: YearMonth,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
    val isFavourite: IsFavorite,
    val isCashed: Boolean,
)