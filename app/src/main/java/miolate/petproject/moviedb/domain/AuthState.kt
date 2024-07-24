package miolate.petproject.moviedb.domain

import kotlinx.coroutines.flow.StateFlow
import miolate.petproject.moviedb.app.base.DataError
import miolate.petproject.moviedb.app.base.DataState

interface AuthState {

    val user: StateFlow<DataState<String, DataError>>
}