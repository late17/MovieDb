package miolate.petproject.moviedb.ui.base

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * A simple base ViewModel utilizing Compose' reactivity.
 */
@Stable
abstract class ComposeViewModel<UiState, UiEvent>(
    initialState: () -> UiState,
    ) : ViewModel() {

    /**
     * Compose ui state.
     * Change this state only via copy methods.
     */
    protected var _state: MutableStateFlow<UiState> = MutableStateFlow(initialState.invoke())
    val uiState: StateFlow<UiState> = _state

    /**
     * Sends an event of an action that happened
     * in the UI to be processed in the ViewModel.
     */
    abstract fun onEvent(event: UiEvent)
}
