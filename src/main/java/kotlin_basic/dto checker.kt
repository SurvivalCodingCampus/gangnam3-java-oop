package kotlin_basic

// Task 2: Model — only the data the app actually needs, in app-friendly form.
// Unlike the DTO, nothing here is nullable (except imageUrl) and units are converted.

data class Pokemon(
    val id: Int,
    val name: String,
    val heightM: Double,          // meters (API gives decimeters)
    val weightKg: Double,         // kilograms (API gives hectograms)
    val types: List<String>,      // e.g. ["electric"], in slot order
    val abilities: List<Ability>,
    val stats: Stats,
    val imageUrl: String?,        // official artwork, falls back to the small sprite
)

data class Ability(
    val name: String,
    val isHidden: Boolean,
)

data class Stats(
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int,
) {
    val total: Int get() = hp + attack + defense + specialAttack + specialDefense + speed
}