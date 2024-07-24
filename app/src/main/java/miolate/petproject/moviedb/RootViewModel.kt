package miolate.petproject.moviedb

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import miolate.petproject.moviedb.app.base.DataError
import miolate.petproject.moviedb.app.base.DataState
import miolate.petproject.moviedb.domain.AuthState
import java.time.Duration
import javax.inject.Inject

private const val DELAY_BEFORE_SHOWING_SCREEN = 200L

@HiltViewModel
class RootViewModel @Inject constructor(private val authProvider: AuthState) : ViewModel() {

    val keepLockScreen: MutableState<Boolean> = mutableStateOf(true)
    var isAuthenticated: MutableStateFlow<DataState<String, DataError>> =
        MutableStateFlow(DataState.Loading())

    init {
        viewModelScope.launch {
            authProvider.user.collectLatest {
                when (it) {
                    is DataState.Loading -> {}
                    else -> {
                        isAuthenticated.value = it
                        delay(Duration.ofMillis(DELAY_BEFORE_SHOWING_SCREEN))
                        keepLockScreen.value = false
                        this.cancel()
                    }
                }
            }
        }
    }
}