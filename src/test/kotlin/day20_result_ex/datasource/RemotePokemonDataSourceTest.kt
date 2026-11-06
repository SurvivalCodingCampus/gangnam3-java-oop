package com.survivalcoding.day20_result_ex.data_source

import io.ktor.client.engine.mock.*

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