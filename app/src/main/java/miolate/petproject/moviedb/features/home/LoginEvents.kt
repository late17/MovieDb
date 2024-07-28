package miolate.petproject.moviedb.features.home

sealed interface HomeEvents {
    data class AddToFavorite(val id: Int) : HomeEvents
    data class RemoveFromFavorite(val id: Int) : HomeEvents
    data class ShareMovie(val id: Int) : HomeEvents
    data object LoadNextItems : HomeEvents
    data object PullRefresh : HomeEvents
}