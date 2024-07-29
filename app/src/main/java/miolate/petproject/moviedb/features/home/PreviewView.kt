package miolate.petproject.moviedb.features.home

import miolate.petproject.moviedb.domain.model.IsFavorite
import miolate.petproject.moviedb.domain.model.Movie
import miolate.petproject.moviedb.features.home.data.HomeState
import java.time.LocalDate
import java.time.YearMonth

internal fun getPreviewData(): HomeState {
    val movies = listOf(
        Movie(
            adult = true,
            backdropPath = "https://dummyimage.com/831x619",
            genreIds = listOf(62, 89, 14, 67, 90),
            id = 975218,
            originalLanguage = "pt",
            originalTitle = "Reverse-engineered holistic artificial intelligence",
            overview = "Traditional discuss natural wear. Eight business include nothing growth red. Participant current never action begin example situation become.",
            popularity = 77.052,
            posterPath = "https://placeimg.com/453/1/any",
            releaseDate = LocalDate.now(),
            yearAndMonth = YearMonth.now(),
            title = "American serve magazine.",
            video = false,
            voteAverage = 8.6,
            voteCount = 377,
            isFavourite = IsFavorite.FAVORITE,
            isCashed = true
        ),
        Movie(
            adult = false,
            backdropPath = "https://dummyimage.com/680x80",
            genreIds = listOf(39, 81, 53, 89, 19),
            id = 545263,
            originalLanguage = "bs",
            originalTitle = "Balanced actuating moratorium",
            overview = "Walk half for. Draw coach store top might policy.\nDebate main population ok position what agency. Answer level rest boy them behind.",
            popularity = 104.471,
            posterPath = "https://placeimg.com/800/263/any",
            releaseDate = LocalDate.now(),
            yearAndMonth = YearMonth.now(),
            title = "Safe medical.",
            video = false,
            voteAverage = 1.3,
            voteCount = 810,
            isFavourite = IsFavorite.FAVORITE,
            isCashed = true
        ),
        Movie(
            adult = false,
            backdropPath = "https://www.lorempixel.com/667/119",
            genreIds = listOf(59, 69, 86, 39, 71),
            id = 458459,
            originalLanguage = "sw",
            originalTitle = "Virtual incremental policy",
            overview = "Hotel know relationship include do from history environment. Let sign wish part start major specific front. Part case show newspaper.",
            popularity = 114.655,
            posterPath = "https://dummyimage.com/301x190",
            releaseDate = LocalDate.now().minusMonths(2),
            yearAndMonth = YearMonth.now(),
            title = "Accept.",
            video = true,
            voteAverage = 3.9,
            voteCount = 1354,
            isFavourite = IsFavorite.FAVORITE,
            isCashed = true
        ),
        Movie(
            adult = true,
            backdropPath = "https://placekitten.com/282/600",
            genreIds = listOf(29, 15, 25, 98, 46),
            id = 891438,
            originalLanguage = "eu",
            originalTitle = "Quality-focused discrete help-desk",
            overview = "Store small blue growth shoulder reduce. Ok fill your change. Finally become good moment region case.\nFind bill learn act. Community about phone light crime any. Base my ready product red.",
            popularity = 8.702,
            posterPath = "https://placekitten.com/866/227",
            releaseDate = LocalDate.now().minusMonths(2),
            yearAndMonth = YearMonth.now(),
            title = "Already from.",
            video = true,
            voteAverage = 2.7,
            voteCount = 540,
            isFavourite = IsFavorite.FAVORITE,
            isCashed = true
        ),
        Movie(
            adult = true,
            backdropPath = "https://placeimg.com/862/523/any",
            genreIds = listOf(54, 7, 27, 2, 66),
            id = 730907,
            originalLanguage = "ar",
            originalTitle = "Customizable client-driven customer loyalty",
            overview = "Tonight amount not. Soldier value foreign of. Side often walk society deal just.\nThus ok institution question. Glass happen it soldier national rule. Instead message loss weight.",
            popularity = 69.414,
            posterPath = "https://placekitten.com/750/496",
            releaseDate = LocalDate.now().minusMonths(4),
            yearAndMonth = YearMonth.now(),
            title = "Issue.",
            video = false,
            voteAverage = 1.8,
            voteCount = 1232,
            isFavourite = IsFavorite.FAVORITE,
            isCashed = true
        ),
    )
    val homeState = HomeState(movies = movies)
    return homeState
}