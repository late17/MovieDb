package miolate.petproject.moviedb.features.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import miolate.petproject.moviedb.domain.MoviesRepository
import miolate.petproject.moviedb.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : BaseViewModel<HomeState, HomeEvents>({ HomeState() }) {

    init {
        viewModelScope.launch {
            moviesRepository.getMovies()
        }
    }

    override fun onEvent(event: HomeEvents) {
        TODO("Not yet implemented")
    }
}