package miolate.petproject.moviedb.features.home.data

sealed class ScreenStatus {
    data object Loading: ScreenStatus();
    data object NoInternet: ScreenStatus();
    data object Success: ScreenStatus();
}