package miolate.petproject.moviedb.data.local

import miolate.petproject.moviedb.data.local.entities.MovieEntity
import miolate.petproject.moviedb.domain.model.IsFavorite
import miolate.petproject.moviedb.domain.model.Movie

abstract class BaseRepository {

    protected fun Movie.toMovieEntity(): MovieEntity {
        return MovieEntity(
            id = this.id,
            adult = this.adult,
            backdropPath = this.backdropPath,
            genreIds = this.genreIds,
            originalLanguage = this.originalLanguage,
            originalTitle = this.originalTitle,
            overview = this.overview,
            popularity = this.popularity,
            posterPath = this.posterPath,
            releaseDate = this.releaseDate,
            yearAndMonthUI = this.yearAndMonthUI,
            title = this.title,
            video = this.video,
            voteAverage = this.voteAverage,
            voteCount = this.voteCount,
            isFavorite = this.isFavourite == IsFavorite.FAVORITE,
            isCashed = this.isCashed
        )
    }

    protected fun MovieEntity.toMovie(): Movie {
        return Movie(
            id = this.id,
            adult = this.adult,
            backdropPath = this.backdropPath,
            genreIds = this.genreIds,
            originalLanguage = this.originalLanguage,
            originalTitle = this.originalTitle,
            overview = this.overview,
            popularity = this.popularity,
            posterPath = this.posterPath,
            releaseDate = this.releaseDate,
            yearAndMonthUI = this.yearAndMonthUI,
            title = this.title,
            video = this.video,
            voteAverage = this.voteAverage,
            voteCount = this.voteCount,
            isFavourite = if (this.isFavorite)
                IsFavorite.FAVORITE
            else
                IsFavorite.NOT_FAVORITE,
            isCashed = this.isCashed
        )
    }
}