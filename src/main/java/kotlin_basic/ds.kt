package kotlin_basic


// Task 3: Mapper — DTO -> Model
// Rules:
//  1. name must not be empty (null / "" / "   " -> default name)
//  2. invalid data -> default values
//  3. never throws: whatever comes in, a Pokemon comes out

object PokemonDefaults {
    const val ID = 0
    const val NAME = "unknown"
    const val MAX_STAT = 255          // highest base stat in the games

    val EMPTY = Pokemon(
        id = ID,
        name = NAME,
        heightM = 0.0,
        weightKg = 0.0,
        types = emptyList(),
        abilities = emptyList(),
        stats = Stats(0, 0, 0, 0, 0, 0),
        imageUrl = null,
    )
}

object PokemonMapper {

    // DTO -> Model. Never throws.
    fun map(dto: PokemonDto?): Pokemon {
        if (dto == null) return PokemonDefaults.EMPTY
        return try {
            mapUnsafe(dto)
        } catch (e: Exception) {
            PokemonDefaults.EMPTY      // last safety net: the game keeps running
        }
    }

    // JSON text -> Model. Broken JSON, wrong types, empty text: all return EMPTY instead of crashing.
    fun parse(json: String?): Pokemon {
        if (json.isNullOrBlank()) return PokemonDefaults.EMPTY
        return try {
            map(pokeJson.decodeFromString<PokemonDto>(json))
        } catch (e: Exception) {       // SerializationException, IllegalArgumentException, ...
            PokemonDefaults.EMPTY
        }
    }

    fun isValidName(name: String?): Boolean = !name.isNullOrBlank()

    private fun mapUnsafe(dto: PokemonDto): Pokemon {
        // "hp" -> 35, "attack" -> 55, ... (entries with no name are skipped)
        val statMap: Map<String, Int?> = dto.stats.orEmpty()
            .mapNotNull { s -> cleanName(s.stat?.name)?.let { it to s.baseStat } }
            .toMap()

        return Pokemon(
            id = validId(dto.id),
            name = validName(dto.name),
            heightM = toTenths(dto.height),
            weightKg = toTenths(dto.weight),
            types = dto.types.orEmpty()
                .sortedBy { it.slot ?: Int.MAX_VALUE }          // missing slot goes last
                .mapNotNull { cleanName(it.type?.name) }        // drop null / blank names
                .distinct(),                                     // no duplicate types
            abilities = dto.abilities.orEmpty()
                .mapNotNull { slot ->
                    cleanName(slot.ability?.name)?.let { Ability(name = it, isHidden = slot.isHidden ?: false) }
                }
                .distinctBy { it.name },
            stats = Stats(
                hp = validStat(statMap["hp"]),
                attack = validStat(statMap["attack"]),
                defense = validStat(statMap["defense"]),
                specialAttack = validStat(statMap["special-attack"]),
                specialDefense = validStat(statMap["special-defense"]),
                speed = validStat(statMap["speed"]),
            ),
            imageUrl = validUrl(dto.sprites?.other?.officialArtwork?.frontDefault)
                ?: validUrl(dto.sprites?.frontDefault),
        )
    }

    private fun validId(id: Int?): Int =
        if (id != null && id > 0) id else PokemonDefaults.ID

    private fun validName(name: String?): String =
        if (isValidName(name)) name!!.trim().lowercase() else PokemonDefaults.NAME

    private fun cleanName(name: String?): String? =
        name?.trim()?.lowercase()?.takeIf { it.isNotEmpty() }

    // decimeters -> meters, hectograms -> kilograms; missing or negative -> 0.0
    private fun toTenths(value: Int?): Double =
        if (value != null && value > 0) value / 10.0 else 0.0

    // missing -> 0, negative -> 0, too big -> 255
    private fun validStat(value: Int?): Int =
        (value ?: 0).coerceIn(0, PokemonDefaults.MAX_STAT)

    // only real http(s) links count as an image
    private fun validUrl(url: String?): String? =
        url?.trim()?.takeIf { it.startsWith("https://") || it.startsWith("http://") }
}

// Keeps the old call style working: dto.toModel()
fun PokemonDto.toModel(): Pokemon = PokemonMapper.map(this)