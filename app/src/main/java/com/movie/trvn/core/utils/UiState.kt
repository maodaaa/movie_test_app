package com.movie.trvn.core.utils

sealed class UiState<out T> {
    data object Loading: UiState<Nothing>()
    data class Success<T>(val data: T): UiState<T>()
    data class Failure(val message: String): UiState<Nothing>()
}