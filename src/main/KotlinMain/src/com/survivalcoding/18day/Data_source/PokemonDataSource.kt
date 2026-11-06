package com.survivalcoding.`18day`.Data_source

import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result
import com.survivalcoding.`18day`.model.Pokemon

interface PokemonDataSource {
    fun getPokemon(name: String): Result<Pokemon, NetworkError>
}
