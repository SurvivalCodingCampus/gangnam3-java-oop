package day18.mock

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout

object FakeHttpClientFactory {
    fun create(
        engine: HttpClientEngine,
        requestTimeoutMillis: Long = 10_000,
    ): HttpClient {
        return HttpClient(engine) {
            install(HttpTimeout) {
                this.requestTimeoutMillis = requestTimeoutMillis
            }
        }
    }
}
