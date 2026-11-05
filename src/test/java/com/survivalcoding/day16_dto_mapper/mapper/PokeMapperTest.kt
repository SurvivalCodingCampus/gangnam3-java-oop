package com.survivalcoding.day16_dto_mapper.mapper

import com.survivalcoding.day16_dto_mapper.dto.PokeDto
import com.survivalcoding.day16_dto_mapper.model.Poke
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PokeMapperTest {

    @Test
    fun `정상 데이터는 그대로 변환된다`() {
        // given
        val dto = PokeDto(
            id = 25,
            name = "pikachu",
            baseExperience = 112,
            height = 4,
            isDefault = true,
            order = 35,
            weight = 60
        )

        // when
        val poke = dto.toModel()

        // then
        assertEquals(
            Poke(
                id = 25,
                name = "pikachu",
                baseExperience = 112,
                height = 4,
                isDefault = true,
                order = 35,
                weight = 60
            ), poke
        )
    }

    @Test
    fun `모든 필드가 null이면 기본값으로 변환된다`() {
        val poke = PokeDto().toModel()

        assertEquals(
            Poke(
                id = 0,
                name = "Unknown",
                baseExperience = 0,
                height = 0,
                isDefault = false,
                order = 0,
                weight = 0
            ), poke
        )
    }

    @Test
    fun `name이 빈 문자열이면 Unknown이 된다`() {
        assertEquals("Unknown", PokeDto(name = "").toModel().name)
    }

    @Test
    fun `name이 공백이면 Unknown이 된다`() {
        assertEquals("Unknown", PokeDto(name = "   ").toModel().name)
    }

    @Test
    fun `height와 weight가 음수면 0이 된다`() {
        val poke = PokeDto(height = -5, weight = -10).toModel()

        assertEquals(0, poke.height)
        assertEquals(0, poke.weight)
    }

    @Test
    fun `height와 weight가 0이면 그대로 0이다`() {
        val poke = PokeDto(height = 0, weight = 0).toModel()

        assertEquals(0, poke.height)
        assertEquals(0, poke.weight)
    }
}
