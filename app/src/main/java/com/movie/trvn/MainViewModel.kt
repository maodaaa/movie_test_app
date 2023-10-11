package com.movie.trvn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.trvn.core.domain.model.Movie
import com.movie.trvn.core.domain.usecase.MoviesUseCase
import com.movie.trvn.core.utils.UiState
import kotlinx.coroutines.launch

class MainViewModel(private val moviesUseCase: MoviesUseCase) : ViewModel() {
    private val _moviesLiveData = MutableLiveData<UiState<List<Movie>>>()
    val moviesLiveData: LiveData<UiState<List<Movie>>>
        get() = _moviesLiveData

    fun getAllMovies() {
        viewModelScope.launch {
            try {
                _moviesLiveData.value = UiState.Loading
                val movies = moviesUseCase.getAllMovies()
                _moviesLiveData.value = UiState.Success(movies)
            } catch (e: Exception) {
                _moviesLiveData.value = UiState.Failure(e.message ?: "An error occurred")
            }
        }
    }

//    fun insertMovies(movieList: List<Movie>) {
//        viewModelScope.launch {
//            try {
//                moviesUseCase.deleteAll()
//                moviesUseCase.insertAllMovies(movieList.take(10))
//                _moviesLiveData.value = UiState.Refresh
//            } catch (e: Exception) {
//                _moviesLiveData.value = UiState.Failure(e.message ?: "An error occurred")
//            }
//        }
//    }
}