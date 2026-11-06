package com.survivalcoding.`18day`.core

sealed class Result<out T, out E> {
    data class Success<out T>(val value: T) : Result<T, Nothing>()
    data class Failure<out E>(val error: E) : Result<Nothing, E>()
    data class Loading<out T>(val value: T? = null) : Result<T, Nothing>()
    data class Id<out T>(val value: T) : Result<T, Nothing>()
    data class Error<out E>(val error: E) : Result<Nothing, E>()
}
