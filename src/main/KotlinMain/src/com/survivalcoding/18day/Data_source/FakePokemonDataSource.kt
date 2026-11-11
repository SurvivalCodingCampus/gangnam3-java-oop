package com.survivalcoding.`18day`.Data_source

import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result
import com.survivalcoding.`18day`.model.Pokemon

class FakePokemonDataSource : PokemonDataSource {

    private val pokemonMap = mapOf(
        "pikachu" to Pokemon(
            id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 35, attack = 55, defense = 40,
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png",
            soundUrl = "sounds/pikachu_cry.wav"
        ),
        "charizard" to Pokemon(
            id = 6, name = "charizard", type = "fire", height = 17, weight = 905,
            baseExperience = 240, hp = 78, attack = 84, defense = 78,
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/6.png",
            soundUrl = "sounds/charizard_cry.wav"
        ),
        "bulbasaur" to Pokemon(
            id = 1, name = "bulbasaur", type = "grass", height = 7, weight = 69,
            baseExperience = 64, hp = 45, attack = 49, defense = 49,
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
            soundUrl = "sounds/bulbasaur_cry.wav"
        ),
        "squirtle" to Pokemon(
            id = 7, name = "squirtle", type = "water", height = 5, weight = 90,
            baseExperience = 63, hp = 44, attack = 48, defense = 65,
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/7.png",
            soundUrl = "sounds/squirtle_cry.wav"
        ),
        "jigglypuff" to Pokemon(
            id = 39, name = "jigglypuff", type = "normal", height = 5, weight = 55,
            baseExperience = 95, hp = 115, attack = 45, defense = 20,
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/39.png",
            soundUrl = "sounds/jigglypuff_cry.wav"
        ),
        "meowth" to Pokemon(
            id = 52, name = "meowth", type = "normal", height = 4, weight = 42,
            baseExperience = 69, hp = 40, attack = 45, defense = 35,
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/52.png",
            soundUrl = "sounds/meowth_cry.wav"
        ),
        "psyduck" to Pokemon(
            id = 54, name = "psyduck", type = "water", height = 8, weight = 196,
            baseExperience = 64, hp = 50, attack = 52, defense = 48,
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/54.png",
            soundUrl = "sounds/psyduck_cry.wav"
        ),
        "snorlax" to Pokemon(
            id = 143, name = "snorlax", type = "normal", height = 21, weight = 4600,
            baseExperience = 154, hp = 160, attack = 110, defense = 65,
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/143.png",
            soundUrl = "sounds/snorlax_cry.wav"
        ),
        "unknown" to Pokemon(
            id = 999, name = "unknown", type = "unknown", height = 0, weight = 0,
            baseExperience = 0, hp = 0, attack = 0, defense = 0,
            imageUrl = null,
            soundUrl = null
        ),
    )

    override fun getPokemon(name: String): Result<Pokemon, NetworkError> {
        return when (val pokemon = pokemonMap[name.lowercase()]) {
            null -> Result.Error(NetworkError.CLIENT_ERROR)
            else -> Result.Success(pokemon)
        }
    }
}
