package miolate.petproject.moviedb.features.home

sealed interface HomeEvents {
    data class LikeMovie(val id: Int) : HomeEvents
    data class ShareMovie(val id: Int) : HomeEvents
    data object LoadNextItems : HomeEvents
    data object PullRefresh : HomeEvents
}