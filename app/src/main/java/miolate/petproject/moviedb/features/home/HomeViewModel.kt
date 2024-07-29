package miolate.petproject.moviedb.features.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import miolate.petproject.moviedb.app.base.DataError
import miolate.petproject.moviedb.app.base.DataResult
import miolate.petproject.moviedb.data.remote.THE_MOVIE_DB_MOVIE_URL
import miolate.petproject.moviedb.domain.MoviesRepository
import miolate.petproject.moviedb.domain.model.IsFavorite
import miolate.petproject.moviedb.domain.model.Movie
import miolate.petproject.moviedb.features.home.data.DefaultPaginator
import miolate.petproject.moviedb.features.home.data.HomeEvents
import miolate.petproject.moviedb.features.home.data.HomeState
import miolate.petproject.moviedb.features.home.data.Paginator
import miolate.petproject.moviedb.features.home.data.ScreenStatus
import miolate.petproject.moviedb.ui.base.BaseViewModel
import miolate.petproject.moviedb.ui.events.Action
import java.time.YearMonth
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

    @Stable
    val groupedMovies: StateFlow<Map<YearMonth, List<Movie>>> = uiState.map { state ->
        val movies = if (uiState.value.status == ScreenStatus.NoInternet)
            state.cashMovies
        else
            state.movies

        val toSortedMap = movies.map { m ->
            if (state.favouritesMovies.any { it.id == m.id })
                m.copy(isFavourite = IsFavorite.FAVORITE)
            else
                m
        }
            .groupBy { it.yearAndMonth }
            .toSortedMap(Comparator.reverseOrder())

        return@map toSortedMap
    }.stateIn(viewModelScope, started = SharingStarted.Eagerly, emptyMap())

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
        onError = { error ->
            handleError(error)
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
                    movies = (uiState.movies + items)
                        .distinctBy { it.id },
                )
            }
            setScreenStatus(ScreenStatus.Success)
        },
        onReset = {
            _uiState.update {
                it.copy(
                    movies = emptyList(),
                    page = 1,
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
        _actions.tryEmit(HomeActions.ShareMovie(THE_MOVIE_DB_MOVIE_URL + id))
    }

    private fun removeFromFavorite(id: Int) {
        viewModelScope.launch {
            uiState.value.favouritesMovies.firstOrNull { it.id == id }?.let { movie ->
                moviesRepository.removeFromFavorites(movie)
            }
        }
    }

    private fun addToFavorite(id: Int) {
        viewModelScope.launch {
            groupedMovies.value.values.flatten().firstOrNull { it.id == id }?.let { movie ->
                moviesRepository.addToFavorites(movie)
            }
        }
    }

    private fun resetScreen() {
        viewModelScope.launch {
            setScreenStatus(ScreenStatus.Loading)
            moviePagination.reset()
            moviePagination.loadNextItems()
        }
    }

    private fun loadNextItems() {
        viewModelScope.launch {
            if (uiState.value.status != ScreenStatus.NoInternet)
                moviePagination.loadNextItems()
        }
    }

    private suspend fun handleError(error: DataResult.Failure<List<Movie>, DataError>) {
        _actions.tryEmit(
            HomeActions.ShowToast(error.e)
        )

        when (error.e) {
            DataError.Network.NO_INTERNET -> {
                loadCashedMovies()
            }

            else -> {
                // New Error Screen Status can be added
                setScreenStatus(ScreenStatus.Loading)
            }
        }
    }

    private suspend fun loadCashedMovies() {
        val cashMovies = moviesRepository.getCashMovies()
        _uiState.update {
            it.copy(
                cashMovies = cashMovies
            )
        }
        setScreenStatus(ScreenStatus.NoInternet)
    }

    private fun setScreenStatus(screenStatus: ScreenStatus) {
        _uiState.update {
            it.copy(
                status = screenStatus
            )
        }
    }

    sealed class HomeActions : Action {
        data class ShowToast(val error: DataError?) : HomeActions()
        data class ShareMovie(val link: String): HomeActions()
    }
}