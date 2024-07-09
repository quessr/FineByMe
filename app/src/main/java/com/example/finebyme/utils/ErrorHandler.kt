package com.example.finebyme.utils

import android.util.Log
import com.example.finebyme.common.enums.ErrorType
import java.io.IOException
import java.net.UnknownHostException

object ErrorHandler {
    fun handleFailure(throwable: Throwable): String {
        return when (throwable) {
            is IOException -> {
                Log.e("PhotoListViewModel", "Network error: ${throwable.message}")
                ErrorType.NETWORK_ERROR.message
            }

            is retrofit2.HttpException -> {
                val message = when (throwable.code()) {
                    400 -> ErrorType.BAD_REQUEST.message
                    401 -> ErrorType.UNAUTHORIZED.message
                    403 -> ErrorType.FORBIDDEN.message
                    404 -> ErrorType.NOT_FOUND.message
                    500, 503 -> ErrorType.SERVER_ERROR.message
                    else -> "HTTP error: ${throwable.message()}"
                }
                Log.e("PhotoListViewModel", "HTTP error: ${throwable.message}")
                message
            }

            is UnknownHostException -> {
                Log.e("PhotoListViewModel", "No internet connection: ${throwable.message}")
                ErrorType.NO_INTERNET.message
            }

            else -> {
                Log.e("PhotoListViewModel", "Unknown error: ${throwable.message}")
                ErrorType.UNKNOWN_ERROR.message
            }
        }
    }
}