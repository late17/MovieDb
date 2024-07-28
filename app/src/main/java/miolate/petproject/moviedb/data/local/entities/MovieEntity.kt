package miolate.petproject.moviedb.data.local.entities

import androidx.room.*
import java.time.LocalDate

@Entity(tableName = "movie")
data class MovieEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val adult: Boolean,
    val backdropPath: String?,
    val genreIds: List<Int>,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    @ColumnInfo(name = "releaseDate") val releaseDate: LocalDate,
    @ColumnInfo(name = "yearAndMonthUI") val yearAndMonthUI: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
    val isFavorite: Boolean,
    val isCashed: Boolean,
)