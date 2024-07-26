package miolate.petproject.moviedb.features.home

sealed interface HomeEvents {
    data object LoadNextItems : HomeEvents
}