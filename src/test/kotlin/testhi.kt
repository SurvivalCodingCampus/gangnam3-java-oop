package kotlin_basic

import kotlin.test.*

class PokemonMapperTest {

    // ---------- helper: a normal, complete Pikachu DTO ----------
    private fun res(name: String?) = NamedApiResourceDto(name = name, url = "https://pokeapi.co/")

    private fun pikachuDto() = PokemonDto(
        id = 25,
        name = "pikachu",
        height = 4,
        weight = 60,
        types = listOf(TypeSlotDto(slot = 1, type = res("electric"))),
        abilities = listOf(
            AbilitySlotDto(ability = res("static"), isHidden = false, slot = 1),
            AbilitySlotDto(ability = res("lightning-rod"), isHidden = true, slot = 3),
        ),
        stats = listOf(
            StatDto(baseStat = 35, stat = res("hp")),
            StatDto(baseStat = 55, stat = res("attack")),
            StatDto(baseStat = 40, stat = res("defense")),
            StatDto(baseStat = 50, stat = res("special-attack")),
            StatDto(baseStat = 50, stat = res("special-defense")),
            StatDto(baseStat = 90, stat = res("speed")),
        ),
        sprites = SpritesDto(
            frontDefault = "https://img/small.png",
            other = OtherSpritesDto(OfficialArtworkDto(frontDefault = "https://img/art.png")),
        ),
    )

    // ---------- 1. normal data ----------
    @Test
    fun `normal pikachu maps every field correctly`() {
        val p = PokemonMapper.map(pikachuDto())
                assertEquals(25, p.id)
        assertEquals("pikachu", p.name)
        assertEquals(0.4, p.heightM)
        assertEquals(6.0, p.weightKg)
        assertEquals(listOf("electric"), p.types)
        assertEquals(listOf(Ability("static", false), Ability("lightning-rod", true)), p.abilities)
        assertEquals(Stats(35, 55, 40, 50, 50, 90), p.stats)
        assertEquals(320, p.stats.total)
        assertEquals("https://img/art.png", p.imageUrl)
    }

    @Test
    fun `toModel extension gives the same result as map`() {
        assertEquals(PokemonMapper.map(pikachuDto()), pikachuDto().toModel())
    }

    // ---------- 2. name checks ----------
    @Test
    fun `null name becomes default`() {
        assertEquals(PokemonDefaults.NAME, PokemonMapper.map(pikachuDto().copy(name = null)).name)
    }

    @Test
    fun `empty name becomes default`() {
        assertEquals(PokemonDefaults.NAME, PokemonMapper.map(pikachuDto().copy(name = "")).name)
    }

    @Test
    fun `whitespace-only name becomes default`() {
        assertEquals(PokemonDefaults.NAME, PokemonMapper.map(pikachuDto().copy(name = "   \t ")).name)
    }

    @Test
    fun `name is trimmed and lowercased`() {
        assertEquals("pikachu", PokemonMapper.map(pikachuDto().copy(name = "  PiKaChu  ")).name)
    }

    @Test
    fun `isValidName detects empty names`() {
        assertFalse(PokemonMapper.isValidName(null))
        assertFalse(PokemonMapper.isValidName(""))
        assertFalse(PokemonMapper.isValidName("   "))
        assertTrue(PokemonMapper.isValidName("pikachu"))
    }

    // ---------- 3. invalid numbers -> defaults ----------
    @Test
    fun `null or negative id becomes 0`() {
        assertEquals(0, PokemonMapper.map(pikachuDto().copy(id = null)).id)
        assertEquals(0, PokemonMapper.map(pikachuDto().copy(id = -5)).id)
        assertEquals(0, PokemonMapper.map(pikachuDto().copy(id = 0)).id)
    }

    @Test
    fun `null or negative height and weight become 0`() {
        val p = PokemonMapper.map(pikachuDto().copy(height = -4, weight = null))
        assertEquals(0.0, p.heightM)
        assertEquals(0.0, p.weightKg)
    }

    @Test
    fun `stats out of range are clamped to 0 to 255`() {
        val dto = pikachuDto().copy(
            stats = listOf(
                StatDto(baseStat = -10, stat = res("hp")),
                StatDto(baseStat = 999, stat = res("attack")),
                StatDto(baseStat = null, stat = res("defense")),
            )
        )
        val s = PokemonMapper.map(dto).stats
        assertEquals(0, s.hp)
        assertEquals(255, s.attack)
        assertEquals(0, s.defense)
        assertEquals(0, s.speed)          // missing entirely
    }

    @Test
    fun `stat entries without a name are ignored`() {
        val dto = pikachuDto().copy(
            stats = listOf(StatDto(baseStat = 77, stat = null), StatDto(baseStat = 60, stat = res("hp")))
        )
        assertEquals(60, PokemonMapper.map(dto).stats.hp)
    }

    // ---------- 4. lists ----------
    @Test
    fun `types are sorted by slot`() {
        val dto = pikachuDto().copy(
            types = listOf(TypeSlotDto(2, res("poison")), TypeSlotDto(1, res("grass")))
        )
        assertEquals(listOf("grass", "poison"), PokemonMapper.map(dto).types)
    }

    @Test
    fun `broken and duplicate types are removed`() {
        val dto = pikachuDto().copy(
            types = listOf(
                TypeSlotDto(1, res("fire")),
                TypeSlotDto(2, null),
                TypeSlotDto(3, res("  ")),
                TypeSlotDto(4, res("FIRE")),
            )
        )
        assertEquals(listOf("fire"), PokemonMapper.map(dto).types)
    }

    @Test
    fun `null lists become empty lists`() {
        val p = PokemonMapper.map(pikachuDto().copy(types = null, abilities = null, stats = null))
        assertTrue(p.types.isEmpty())
        assertTrue(p.abilities.isEmpty())
        assertEquals(0, p.stats.total)
    }

    @Test
    fun `abilities with no name are dropped and missing isHidden is false`() {
        val dto = pikachuDto().copy(
            abilities = listOf(
                AbilitySlotDto(ability = null),
                AbilitySlotDto(ability = res("static"), isHidden = null),
            )
        )
        assertEquals(listOf(Ability("static", false)), PokemonMapper.map(dto).abilities)
    }

    // ---------- 5. image ----------
    @Test
    fun `missing artwork falls back to small sprite`() {
        val dto = pikachuDto().copy(sprites = SpritesDto(frontDefault = "https://img/small.png", other = null))
        assertEquals("https://img/small.png", PokemonMapper.map(dto).imageUrl)
    }

    @Test
    fun `blank or non-http artwork falls back to small sprite`() {
        val dto = pikachuDto().copy(
            sprites = SpritesDto(
                frontDefault = "https://img/small.png",
                other = OtherSpritesDto(OfficialArtworkDto(frontDefault = "   ")),
            )
        )
        assertEquals("https://img/small.png", PokemonMapper.map(dto).imageUrl)
    }

    @Test
    fun `no images at all gives null`() {
        assertNull(PokemonMapper.map(pikachuDto().copy(sprites = null)).imageUrl)
    }

    // ---------- 6. never crashes ----------
    @Test
    fun `completely empty dto gives the default pokemon`() {
        assertEquals(PokemonDefaults.EMPTY, PokemonMapper.map(PokemonDto()))
    }

    @Test
    fun `null dto gives the default pokemon`() {
        assertEquals(PokemonDefaults.EMPTY, PokemonMapper.map(null))
    }

    @Test
    fun `broken json does not crash`() {
        assertEquals(PokemonDefaults.EMPTY, PokemonMapper.parse("{ this is not json"))
        assertEquals(PokemonDefaults.EMPTY, PokemonMapper.parse(""))
        assertEquals(PokemonDefaults.EMPTY, PokemonMapper.parse(null))
    }

    @Test
    fun `wrong value type in json does not crash`() {
        assertEquals(PokemonDefaults.EMPTY, PokemonMapper.parse("""{"id": 25, "name": "pikachu", "height": "tall"}"""))
    }

    @Test
    fun `valid json with unknown keys is parsed`() {
        val json = """{"id":25,"name":"pikachu","height":4,"weight":60,"some_new_field":[1,2,3],
            "types":[{"slot":1,"type":{"name":"electric","url":"x"}}]}"""
        val p = PokemonMapper.parse(json)
        assertEquals("pikachu", p.name)
        assertEquals(listOf("electric"), p.types)
    }
}