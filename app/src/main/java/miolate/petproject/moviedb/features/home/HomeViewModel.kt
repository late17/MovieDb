package miolate.petproject.moviedb.features.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import miolate.petproject.moviedb.domain.MoviesRepository
import miolate.petproject.moviedb.domain.model.Movie
import miolate.petproject.moviedb.ui.base.BaseViewModel
import miolate.petproject.moviedb.ui.events.Action
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : BaseViewModel<HomeState, HomeEvents>({ HomeState() }) {

    private val _actions = MutableSharedFlow<HomeActions>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val actions = _actions.asSharedFlow()

    private val moviePagination: Paginator<Int, Movie> = DefaultPaginator(
        initialKey = uiState.value.page,
        onLoadUpdated = { isMakingRequest ->
            _uiState.update {
                it.copy(
                    isLoadingNewItems = isMakingRequest
                )
            }
        },
        onRequest = { nextPage ->
            moviesRepository.getMovies(nextPage)
        },
        getNextKey = {
            uiState.value.page + 1
        },
        onError = {
            _actions.tryEmit(
                HomeActions.ShowError("Show Error")
            )
        },
        onSuccess = { items, newKey ->
            _uiState.update {
                it.copy(
                    movies = _uiState.value.movies + items,
                    page = newKey,
                    endReached = items.isEmpty()
                )
            }
        },
        onReset = {
            _uiState.update {
                it.copy(
                    movies = emptyList(),
                    page = 0,
                    endReached = false
                )
            }
        }
    )

    init {
        viewModelScope.launch {
            moviePagination.loadNextItems()
        }
    }

    override fun onEvent(event: HomeEvents) {
        when(event){
            HomeEvents.LoadNextItems -> loadNextItems()
        }
    }

    private fun loadNextItems() {
        viewModelScope.launch {
            moviePagination.loadNextItems()
        }
    }

    sealed class HomeActions : Action {
        data class ShowError(val errorText: String) : HomeActions()
    }
}