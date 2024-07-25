package miolate.petproject.moviedb.features.login

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import miolate.petproject.moviedb.domain.AuthState
import miolate.petproject.moviedb.ui.base.BaseViewModel
import miolate.petproject.moviedb.ui.navigation.NavigationPath
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authProvider: AuthState
) : BaseViewModel<LoginState, LoginEvents>({ LoginState() }) {

    private val _navigation = MutableSharedFlow<NavigationPath>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigation = _navigation.asSharedFlow()

    override fun onEvent(event: LoginEvents) {
        when (event) {
            LoginEvents.SignIn -> signIn()
        }
    }

    private fun signIn() {
        _navigation.tryEmit(Navigation.NavigateToHome)
    }

    sealed class Navigation : NavigationPath {
        data object NavigateToHome : Navigation()
    }
}