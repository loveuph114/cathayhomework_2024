package cc.reece.cathayhomework_2024.model

sealed class RequestResult<out T> {
    data class Success<T>(val data: T) : RequestResult<T>()
    data class Error(val throwable: Throwable) : RequestResult<Nothing>()
}