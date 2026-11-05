package day18.mock

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object FakeHttpClientFactory {
    fun create(
        engine: HttpClientEngine,
        requestTimeoutMillis: Long = 10_000,
    ): HttpClient {
        return HttpClient(engine) {
            install(HttpTimeout) {
                this.requestTimeoutMillis = requestTimeoutMillis
            }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
}
