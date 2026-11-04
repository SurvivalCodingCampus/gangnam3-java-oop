package com.survivalcoding.day18_http_exam.data_source

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngine.Companion.invoke
import io.ktor.client.engine.mock.respond

val mockEngine = MockEngine { request ->
    when (request.url.toString()) {
        "https://pokeapi.co/api/v2/pokemon/ditto" -> {
            respond(
                content = jsonString,
            )
        }

        else -> error("Unhandled ${request.url}")
    }
}