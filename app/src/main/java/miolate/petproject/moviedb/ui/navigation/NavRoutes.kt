package miolate.petproject.moviedb.ui.navigation

enum class NavRoutes(val route: String) {
    DEFAULT("default"),

    HOME("home"),
    MOVIE("movie/{id}"),
    FAVORITES("favorites"),
    LOGIN("login"),
    PROFILE("profile")
}