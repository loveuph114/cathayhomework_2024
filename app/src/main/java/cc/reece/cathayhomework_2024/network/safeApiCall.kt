package cc.reece.cathayhomework_2024.network

import cc.reece.cathayhomework_2024.model.RequestResult
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): RequestResult<T> {
    return try {
        RequestResult.Success(apiCall.invoke())
    } catch (throwable: Throwable) {
        when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    in 400..499 -> RequestResult.Error(throwable)
                    else -> RequestResult.Error(IOException("Unexpected HTTP error: ${throwable.code()}"))
                }
            }

            is IOException -> RequestResult.Error(throwable)
            else -> RequestResult.Error(IOException("Unexpected error", throwable))
        }
    }
}