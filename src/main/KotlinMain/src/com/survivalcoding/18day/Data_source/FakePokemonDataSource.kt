package com.survivalcoding.`18day`.Data_source

import com.survivalcoding.`18day`.repostory.PokemonRepository

class FakePokemonDataSource: PokemonRepository  {
    override suspend fun getPokemon(name: String) : Response<String> {
        delay
    }


}