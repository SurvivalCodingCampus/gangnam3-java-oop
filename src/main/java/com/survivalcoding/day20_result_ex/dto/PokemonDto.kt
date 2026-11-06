package com.survivalcoding.day20_result_ex.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("base_experience")
    val baseExperience: Int? = null,
    @SerialName("height")
    val height: Int? = null,
    @SerialName("is_default")
    val isDefault: Boolean? = null,
    @SerialName("order")
    val order: Int? = null,
    @SerialName("weight")
    val weight: Int? = null,
    @SerialName("abilities")
    val abilities: List<PokemonAbility?>? = null,
    @SerialName("past_abilities")
    val pastAbilities: List<PastAbility?>? = null,
    @SerialName("forms")
    val forms: List<NamedApiResource?>? = null,
    @SerialName("game_indices")
    val gameIndices: List<VersionGameIndex?>? = null,
    @SerialName("held_items")
    val heldItems: List<PokemonHeldItem?>? = null,
    @SerialName("location_area_encounters")
    val locationAreaEncounters: String? = null,
    @SerialName("moves")
    val moves: List<PokemonMove?>? = null,
    @SerialName("species")
    val species: NamedApiResource? = null,
    @SerialName("sprites")
    val sprites: PokemonSprites? = null,
    @SerialName("cries")
    val cries: PokemonCries? = null,
    @SerialName("stats")
    val stats: List<PokemonStat?>? = null,
    @SerialName("past_stats")
    val pastStats: List<PastStat?>? = null,
    @SerialName("types")
    val types: List<PokemonType?>? = null,
    @SerialName("past_types")
    val pastTypes: List<PastType?>? = null
)

@Serializable
data class NamedApiResource(
    @SerialName("name")
    val name: String? = null,
    @SerialName("url")
    val url: String? = null
)

@Serializable
data class PokemonAbility(
    @SerialName("ability")
    val ability: NamedApiResource? = null,
    @SerialName("is_hidden")
    val isHidden: Boolean? = null,
    @SerialName("slot")
    val slot: Int? = null
)

@Serializable
data class PastAbility(
    @SerialName("generation")
    val generation: NamedApiResource? = null,
    @SerialName("abilities")
    val abilities: List<PokemonAbility?>? = null
)

@Serializable
data class VersionGameIndex(
    @SerialName("game_index")
    val gameIndex: Int? = null,
    @SerialName("version")
    val version: NamedApiResource? = null
)

@Serializable
data class PokemonHeldItem(
    @SerialName("item")
    val item: NamedApiResource? = null,
    @SerialName("version_details")
    val versionDetails: List<PokemonHeldItemVersion?>? = null
)

@Serializable
data class PokemonHeldItemVersion(
    @SerialName("rarity")
    val rarity: Int? = null,
    @SerialName("version")
    val version: NamedApiResource? = null
)

@Serializable
data class PokemonMove(
    @SerialName("move")
    val move: NamedApiResource? = null,
    @SerialName("version_group_details")
    val versionGroupDetails: List<PokemonMoveVersion?>? = null
)

@Serializable
data class PokemonMoveVersion(
    @SerialName("level_learned_at")
    val levelLearnedAt: Int? = null,
    @SerialName("move_learn_method")
    val moveLearnMethod: NamedApiResource? = null,
    @SerialName("version_group")
    val versionGroup: NamedApiResource? = null,
    @SerialName("order")
    val order: Int? = null
)

@Serializable
data class PokemonCries(
    @SerialName("latest")
    val latest: String? = null,
    @SerialName("legacy")
    val legacy: String? = null
)

@Serializable
data class PokemonStat(
    @SerialName("base_stat")
    val baseStat: Int? = null,
    @SerialName("effort")
    val effort: Int? = null,
    @SerialName("stat")
    val stat: NamedApiResource? = null
)

@Serializable
data class PastStat(
    @SerialName("generation")
    val generation: NamedApiResource? = null,
    @SerialName("stats")
    val stats: List<PokemonStat?>? = null
)

@Serializable
data class PokemonType(
    @SerialName("slot")
    val slot: Int? = null,
    @SerialName("type")
    val type: NamedApiResource? = null
)

@Serializable
data class PastType(
    @SerialName("generation")
    val generation: NamedApiResource? = null,
    @SerialName("types")
    val types: List<PokemonType?>? = null
)

@Serializable
data class PokemonSprites(
    @SerialName("front_default")
    val frontDefault: String? = null,
    @SerialName("front_shiny")
    val frontShiny: String? = null,
    @SerialName("front_female")
    val frontFemale: String? = null,
    @SerialName("front_shiny_female")
    val frontShinyFemale: String? = null,
    @SerialName("back_default")
    val backDefault: String? = null,
    @SerialName("back_shiny")
    val backShiny: String? = null,
    @SerialName("back_female")
    val backFemale: String? = null,
    @SerialName("back_shiny_female")
    val backShinyFemale: String? = null,
    @SerialName("other")
    val other: SpriteOther? = null,
    @SerialName("versions")
    val versions: SpriteVersions? = null
)

@Serializable
data class SpriteOther(
    @SerialName("dream_world")
    val dreamWorld: SpriteDreamWorld? = null,
    @SerialName("home")
    val home: SpriteHome? = null,
    @SerialName("official-artwork")
    val officialArtwork: SpriteOfficialArtwork? = null,
    @SerialName("showdown")
    val showdown: SpriteBaseAnimation? = null
)

@Serializable
data class SpriteDreamWorld(
    @SerialName("front_default")
    val frontDefault: String? = null,
    @SerialName("front_female")
    val frontFemale: String? = null
)

@Serializable
data class SpriteHome(
    @SerialName("front_default")
    val frontDefault: String? = null,
    @SerialName("front_female")
    val frontFemale: String? = null,
    @SerialName("front_shiny")
    val frontShiny: String? = null,
    @SerialName("front_shiny_female")
    val frontShinyFemale: String? = null
)

@Serializable
data class SpriteOfficialArtwork(
    @SerialName("front_default")
    val frontDefault: String? = null,
    @SerialName("front_shiny")
    val frontShiny: String? = null,
    @SerialName("versions")
    val versions: SpriteArtworkVersions? = null
)

@Serializable
data class SpriteArtworkVersions(
    @SerialName("generation-i")
    val generationI: SpriteArtworkGenI? = null,
    @SerialName("generation-ii")
    val generationII: SpriteArtworkGenII? = null
)

@Serializable
data class SpriteArtworkGenI(
    @SerialName("red-and-blue")
    val redAndBlue: SpriteDefaultOnly? = null,
    @SerialName("red-and-green")
    val redAndGreen: SpriteDefaultOnly? = null
)

@Serializable
data class SpriteArtworkGenII(
    @SerialName("gold-and-silver")
    val goldAndSilver: SpriteDefaultOnly? = null
)

@Serializable
data class SpriteDefaultOnly(
    @SerialName("front_default")
    val frontDefault: String? = null
)

@Serializable
data class SpriteVersions(
    @SerialName("generation-i")
    val generationI: SpriteGenI? = null,
    @SerialName("generation-ii")
    val generationII: SpriteGenII? = null,
    @SerialName("generation-iii")
    val generationIII: SpriteGenIII? = null,
    @SerialName("generation-iv")
    val generationIV: SpriteGenIV? = null,
    @SerialName("generation-v")
    val generationV: SpriteGenV? = null,
    @SerialName("generation-vi")
    val generationVI: SpriteGenVI? = null,
    @SerialName("generation-vii")
    val generationVII: SpriteGenVII? = null,
    @SerialName("generation-viii")
    val generationVIII: SpriteGenVIII? = null,
    @SerialName("generation-ix")
    val generationIX: SpriteGenIX? = null
)

@Serializable
data class SpriteGenI(
    @SerialName("red-blue")
    val redBlue: SpriteClassicGb? = null,
    @SerialName("yellow")
    val yellow: SpriteClassicYellow? = null,
    @SerialName("red-green-japan")
    val redGreenJapan: SpriteClassicJapan? = null
)

@Serializable
data class SpriteClassicGb(
    @SerialName("back_default")
    val backDefault: String? = null,
    @SerialName("back_gray")
    val backGray: String? = null,
    @SerialName("back_transparent")
    val backTransparent: String? = null,
    @SerialName("back_transparent_gray")
    val backTransparentGray: String? = null,
    @SerialName("front_default")
    val frontDefault: String? = null,
    @SerialName("front_gray")
    val frontGray: String? = null,
    @SerialName("front_transparent")
    val frontTransparent: String? = null,
    @SerialName("front_transparent_gray")
    val frontTransparentGray: String? = null
)

@Serializable
data class SpriteClassicYellow(
    @SerialName("back_default")
    val backDefault: String? = null,
    @SerialName("back_gbc")
    val backGbc: String? = null,
    @SerialName("back_gray")
    val backGray: String? = null,
    @SerialName("back_transparent")
    val backTransparent: String? = null,
    @SerialName("back_transparent_gray")
    val backTransparentGray: String? = null,
    @SerialName("front_default")
    val frontDefault: String? = null,
    @SerialName("front_gbc")
    val frontGbc: String? = null,
    @SerialName("front_gray")
    val frontGray: String? = null,
    @SerialName("front_transparent")
    val frontTransparent: String? = null,
    @SerialName("front_transparent_gray")
    val frontTransparentGray: String? = null
)

@Serializable
data class SpriteClassicJapan(
    @SerialName("back_default")
    val backDefault: String? = null,
    @SerialName("back_gray")
    val backGray: String? = null,
    @SerialName("front_default")
    val frontDefault: String? = null,
    @SerialName("front_gray")
    val frontGray: String? = null
)

@Serializable
data class SpriteGenII(
    @SerialName("crystal")
    val crystal: SpriteCrystal? = null,
    @SerialName("gold")
    val gold: SpriteGoldSilver? = null,
    @SerialName("silver")
    val silver: SpriteGoldSilver? = null
)

@Serializable
data class SpriteGoldSilver(
    @SerialName("back_default")
    val backDefault: String? = null,
    @SerialName("back_shiny")
    val backShiny: String? = null,
    @SerialName("front_default")
    val frontDefault: String? = null,
    @SerialName("front_shiny")
    val frontShiny: String? = null,
    @SerialName("back_transparent")
    val backTransparent: String? = null,
    @SerialName("front_transparent")
    val frontTransparent: String? = null,
    @SerialName("back_shiny_transparent")
    val backShinyTransparent: String? = null,
    @SerialName("front_shiny_transparent")
    val frontShinyTransparent: String? = null
)

@Serializable
data class SpriteCrystal(
    @SerialName("back_default")
    val backDefault: String? = null,
    @SerialName("back_shiny")
    val backShiny: String? = null,
    @SerialName("front_default")
    val frontDefault: String? = null,
    @SerialName("front_shiny")
    val frontShiny: String? = null,
    @SerialName("back_transparent")
    val backTransparent: String? = null,
    @SerialName("front_transparent")
    val frontTransparent: String? = null,
    @SerialName("back_shiny_transparent")
    val backShinyTransparent: String? = null,
    @SerialName("front_shiny_transparent")
    val frontShinyTransparent: String? = null,
    @SerialName("animated")
    val animated: SpriteBaseAnimation? = null
)

@Serializable
data class SpriteGenIII(
    @SerialName("emerald")
    val emerald: SpriteGenIIIAnimation? = null,
    @SerialName("firered-leafgreen")
    val fireredLeafgreen: SpriteGenIIIBase? = null,
    @SerialName("ruby-sapphire")
    val rubySapphire: SpriteGenIIIBase? = null,
    @SerialName("icons")
    val icons: SpriteFrontDefaultIcon? = null
)

@Serializable
data class SpriteGenIIIBase(
    @SerialName("back_default")
    val backDefault: String? = null,
    @SerialName("back_shiny")
    val backShiny: String? = null,
    @SerialName("front_default")
    val frontDefault: String? = null,
    @SerialName("front_shiny")
    val frontShiny: String? = null
)

@Serializable
data class SpriteGenIIIAnimation(
    @SerialName("back_default")
    val backDefault: String? = null,
    @SerialName("back_shiny")
    val backShiny: String? = null,
    @SerialName("front_default")
    val frontDefault: String? = null,
    @SerialName("front_shiny")
    val frontShiny: String? = null,
    @SerialName("animated")
    val animated: SpriteGenIIIBase? = null
)

@Serializable
data class SpriteGenIV(
    @SerialName("diamond-pearl")
    val diamondPearl: SpriteGenIVItem? = null,
    @SerialName("heartgold-soulsilver")
    val heartgoldSoulsilver: SpriteGenIVItem? = null,
    @SerialName("platinum")
    val platinum: SpriteGenIVItem? = null,
    @SerialName("icons")
    val icons: SpriteFrontDefaultIcon? = null
)

@Serializable
data class SpriteGenIVItem(
    @SerialName("back_default")
    val backDefault: String? = null,
    @SerialName("back_female")
    val backFemale: String? = null,
    @SerialName("back_shiny")
    val backShiny: String? = null,
    @SerialName("back_shiny_female")
    val backShinyFemale: String? = null,
    @SerialName("front_default")
    val frontDefault: String? = null,
    @SerialName("front_female")
    val frontFemale: String? = null,
    @SerialName("front_shiny")
    val frontShiny: String? = null,
    @SerialName("front_shiny_female")
    val frontShinyFemale: String? = null,
    @SerialName("animated")
    val animated: SpriteBaseAnimation? = null
)

@Serializable
data class SpriteGenV(
    @SerialName("black-white")
    val blackWhite: SpriteGenIVItem? = null,
    @SerialName("icons")
    val icons: SpriteGenVIcons? = null
)

@Serializable
data class SpriteGenVIcons(
    @SerialName("front_default")
    val frontDefault: String? = null,
    @SerialName("animated")
    val animated: SpriteFrontDefaultIcon? = null
)

@Serializable
data class SpriteGenVI(
    @SerialName("omegaruby-alphasapphire")
    val omegaRubyAlphaSapphire: SpriteBaseAnimation? = null,
    @SerialName("x-y")
    val xy: SpriteBaseAnimation? = null,
    @SerialName("icons")
    val icons: SpriteGenderIcon? = null
)

@Serializable
data class SpriteGenVII(
    @SerialName("icons")
    val icons: SpriteGenderIcon? = null,
    @SerialName("ultra-sun-ultra-moon")
    val ultraSunUltraMoon: SpriteBaseAnimation? = null,
    @SerialName("lets-go-pikachu-lets-go-eevee")
    val letsGoPikachuLetsGoEevee: SpriteBaseAnimation? = null
)

@Serializable
data class SpriteGenVIII(
    @SerialName("icons")
    val icons: SpriteGenderIcon? = null,
    @SerialName("sword-shield")
    val swordShield: SpriteBaseAnimation? = null,
    @SerialName("legends-arceus")
    val legendsArceus: SpriteGenderIcon? = null,
    @SerialName("brilliant-diamond-shining-pearl")
    val brilliantDiamondShiningPearl: SpriteBaseAnimation? = null
)

@Serializable
data class SpriteGenIX(
    @SerialName("champions")
    val champions: SpriteGenIXPlatform? = null,
    @SerialName("legends-za")
    val legendsZa: SpriteGenIXPlatform? = null,
    @SerialName("scarlet-violet")
    val scarletViolet: SpriteGenIXPlatform? = null
)

@Serializable
data class SpriteGenIXPlatform(
    @SerialName("back_default")
    val backDefault: String? = null,
    @SerialName("back_female")
    val backFemale: String? = null,
    @SerialName("back_shiny")
    val backShiny: String? = null,
    @SerialName("back_shiny_female")
    val backShinyFemale: String? = null,
    @SerialName("front_default")
    val frontDefault: String? = null,
    @SerialName("front_female")
    val frontFemale: String? = null,
    @SerialName("front_shiny")
    val frontShiny: String? = null,
    @SerialName("front_shiny_female")
    val frontShinyFemale: String? = null,
    @SerialName("icons")
    val icons: SpriteShinyGenderIcon? = null
)

@Serializable
data class SpriteBaseAnimation(
    @SerialName("back_default")
    val backDefault: String? = null,
    @SerialName("back_female")
    val backFemale: String? = null,
    @SerialName("back_shiny")
    val backShiny: String? = null,
    @SerialName("back_shiny_female")
    val backShinyFemale: String? = null,
    @SerialName("front_default")
    val frontDefault: String? = null,
    @SerialName("front_female")
    val frontFemale: String? = null,
    @SerialName("front_shiny")
    val frontShiny: String? = null,
    @SerialName("front_shiny_female")
    val frontShinyFemale: String? = null
)

@Serializable
data class SpriteFrontDefaultIcon(
    @SerialName("front_default")
    val frontDefault: String? = null
)

@Serializable
data class SpriteGenderIcon(
    @SerialName("front_default")
    val frontDefault: String? = null,
    @SerialName("front_female")
    val frontFemale: String? = null
)

@Serializable
data class SpriteShinyGenderIcon(
    @SerialName("front_default")
    val frontDefault: String? = null,
    @SerialName("front_female")
    val frontFemale: String? = null,
    @SerialName("front_shiny")
    val frontShiny: String? = null,
    @SerialName("front_shiny_female")
    val frontShinyFemale: String? = null
)