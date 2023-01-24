package com.example.lessons.contacts.domain.api.response

import java.io.IOException

sealed interface ApiResponse<out T> {

    class Success<T>(val data: T) : ApiResponse<T>

    sealed interface Failure : ApiResponse<Nothing> {

        data class NetworkFailure(val error: IOException) : Failure

        data class HttpFailure(val code: Int, val message: String) : Failure

        data class UnknownFailure(val error: Throwable) : Failure
    }
}