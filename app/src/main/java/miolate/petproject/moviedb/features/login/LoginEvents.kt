package miolate.petproject.moviedb.features.login

sealed interface LoginEvents{
    data object SignIn : LoginEvents
}