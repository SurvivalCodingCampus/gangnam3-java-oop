package com.survivalcoding.day20

sealed interface Result<out D, out E>

data class Success<out D>(val value: D): Result<D, Nothing>
data class Error<out E>(val error: E): Result<Nothing, E>
