package day18.mock

import com.survivalcoding.day18.MyResponse
import com.survivalcoding.day18.StatusCode
import com.survivalcoding.day18.datasource.PokemonDataSource
import com.survivalcoding.day18.model.Pokemon
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.util.toMap
import kotlinx.serialization.json.Json

class FakePokemonDataSource(
    private val httpClient: HttpClient,
    private val baseUrl: String = "https://pokeapi.co/api/v2/pokemon",
) : PokemonDataSource {

    override suspend fun getPokemonByName(name: String): MyResponse<Pokemon?> {
        val response = httpClient.get("$baseUrl/$name")
        val statusCode = StatusCode(response.status.value)

        return MyResponse(
            statusCode = statusCode,
            headers = response.headers.toMap(),
            body = if (statusCode == StatusCode.Ok) {
                Json.decodeFromString<Pokemon>(response.bodyAsText())
            } else {
                null
            },
        )
    }
}
