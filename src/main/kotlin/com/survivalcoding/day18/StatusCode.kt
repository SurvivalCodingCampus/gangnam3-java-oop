package com.survivalcoding.day18

@JvmInline
value class StatusCode(val value: Int) {
    companion object {
        val Ok = StatusCode(200)
        val NotFound = StatusCode(404)
    }
}
