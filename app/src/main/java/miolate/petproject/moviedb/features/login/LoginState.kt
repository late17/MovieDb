package miolate.petproject.moviedb.features.login

import androidx.compose.runtime.Immutable

@Immutable
data class LoginState(
    val state: String = "",
)