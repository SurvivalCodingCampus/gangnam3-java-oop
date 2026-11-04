package com.survivalcoding.day18_http_exam.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pokemon(
    @SerialName("abilities")
    val abilities: List<Ability>,
    @SerialName("base_experience")
    val baseExperience: Int,
    @SerialName("cries")
    val cries: Cries,
    @SerialName("forms")
    val forms: List<Form>,
    @SerialName("game_indices")
    val gameIndices: List<GameIndice>,
    @SerialName("height")
    val height: Int,
    @SerialName("held_items")
    val heldItems: List<HeldItem>,
    @SerialName("id")
    val id: Int,
    @SerialName("is_default")
    val isDefault: Boolean,
    @SerialName("location_area_encounters")
    val locationAreaEncounters: String,
    @SerialName("moves")
    val moves: List<Move>,
    @SerialName("name")
    val name: String,
    @SerialName("order")
    val order: Int,
    @SerialName("past_abilities")
    val pastAbilities: List<PastAbility>,
    @SerialName("past_stats")
    val pastStats: List<PastStat>,
    @SerialName("species")
    val species: Species,
    @SerialName("sprites")
    val sprites: Sprites,
    @SerialName("stats")
    val stats: List<StatXX>,
    @SerialName("types")
    val types: List<Type>,
    @SerialName("weight")
    val weight: Int
)