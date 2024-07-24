package miolate.petproject.moviedb.domain.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import miolate.petproject.moviedb.app.base.DataError
import miolate.petproject.moviedb.app.base.DataState
import miolate.petproject.moviedb.domain.AuthState

class AuthStateImpl : AuthState {

    private val _user: MutableStateFlow<DataState<String, DataError>> =
        MutableStateFlow(DataState.Failure())
    override val user: StateFlow<DataState<String, DataError>> = _user
}