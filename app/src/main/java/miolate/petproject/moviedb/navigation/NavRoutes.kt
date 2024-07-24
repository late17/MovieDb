package miolate.petproject.moviedb.navigation

enum class NavRoutes(val route: String) {
    HOME("home"),
    MOVIE("movie/{id}"),
    FAVORITES("favorites"),
    LOGIN("login"),
    PROFILE("profile")
}