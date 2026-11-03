package com.survivalcoding.day18

@JvmInline
value class StatusCode(val value: Int) {
    val isSuccess: Boolean
        get() {
            return value in 200..299
        }
}
