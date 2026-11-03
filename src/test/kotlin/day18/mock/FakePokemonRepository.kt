package day18.mock

import com.survivalcoding.day18.StatusCode
import com.survivalcoding.day18.datasource.PokemonDataSource
import com.survivalcoding.day18.model.Pokemon
import com.survivalcoding.day18.repository.inter.PokemonRepository

class FakePokemonRepository(
    private val dataSource: PokemonDataSource,
) : PokemonRepository {

    override suspend fun getPokemonByName(name: String): Pokemon? {
        val response = dataSource.getPokemonByName(name)

        return if (response.statusCode == StatusCode.Ok) {
            response.body
        } else {
            null
        }
    }
}
