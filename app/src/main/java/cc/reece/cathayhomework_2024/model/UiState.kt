package cc.reece.cathayhomework_2024.model

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val result: T) : UiState<T>()
    data class Error(val throwable: Throwable) : UiState<Nothing>()
}