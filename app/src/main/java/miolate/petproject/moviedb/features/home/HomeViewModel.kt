package miolate.petproject.moviedb.features.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import miolate.petproject.moviedb.domain.MoviesRepository
import miolate.petproject.moviedb.domain.model.IsFavorite
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
            _uiState.update { uiState ->
                uiState.copy(
                    page = newKey,
                    endReached = items.isEmpty(),
                    // Distinct By may cause some optimization Issue
                    // But due to API limitation, which in some cases can return
                    // Identical Data (when I didn't set the filters it did)
                    // Should be placed here
                    movies = (_uiState.value.movies + items).distinctBy { it.id }
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
        resetScreen()
        viewModelScope.launch {
            moviesRepository.getFavouritesMovies().collectLatest { movies ->
                _uiState.update {
                    it.copy(
                        favouritesMovies = movies
                    )
                }
            }
        }
    }

    override fun onEvent(event: HomeEvents) {
        when (event) {
            HomeEvents.LoadNextItems -> loadNextItems()
            is HomeEvents.AddToFavorite -> addToFavorite(event.id)
            is HomeEvents.RemoveFromFavorite -> removeFromFavorite(event.id)
            is HomeEvents.ShareMovie -> shareMovie(event.id)
            HomeEvents.PullRefresh -> resetScreen()
        }
    }

    private fun shareMovie(id: Int) {

    }

    private fun removeFromFavorite(id: Int) {
        viewModelScope.launch {
            uiState.value.movies.first{it.id == id}.let { movie ->
                moviesRepository.removeFromFavorites(movie)
                updateMovie(movie.copy(isFavourite = IsFavorite.NOT_FAVORITE))
            }
        }
    }

    private fun addToFavorite(id: Int) {
        viewModelScope.launch {
            uiState.value.movies.first{it.id == id}.let { movie ->
                updateMovie(movie.copy(isFavourite = IsFavorite.FAVORITE))
            }
        }
    }

    private fun updateMovie(movie: Movie) {
        movie.let {
            _uiState.update {
                it.insertMovie(movie)
            }
        }
    }

    private fun resetScreen() {
        viewModelScope.launch {
            setIsLoading(true)
            moviePagination.reset()
            moviePagination.loadNextItems()
            setIsLoading(false)
        }
    }

    private fun loadNextItems() {
        viewModelScope.launch {
            moviePagination.loadNextItems()
        }
    }

    private fun setIsLoading(isLoading: Boolean) {
        _uiState.update {
            _uiState.value.copy(
                isLoading = isLoading
            )
        }
    }

    sealed class HomeActions : Action {
        data class ShowError(val errorText: String) : HomeActions()
    }
}