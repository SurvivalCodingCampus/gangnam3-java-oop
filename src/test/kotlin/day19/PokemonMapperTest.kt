@file:Suppress("NonAsciiCharacters")

package day19

import com.survivalcoding.day18.model.Pokemon
import com.survivalcoding.day19.dto.PokemonDto
import com.survivalcoding.day19.mapper.toDto
import com.survivalcoding.day19.mapper.toModel
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test

class PokemonMapperTest {

    private fun dto(
        id: Int? = 25,
        name: String? = "pikachu",
        baseExperience: Int? = 112,
        height: Int? = 4,
        isDefault: Boolean? = true,
        order: Int? = 35,
        weight: Int? = 60,
    ) = PokemonDto(
        id,
        name,
        baseExperience,
        height,
        isDefault,
        order,
        weight
    )

    @Test
    fun `유효한 DTO는 값 그대로 Model로 변환된다`() {
        val model = dto().toModel()

        assertThat(model).isEqualTo(
            Pokemon(
                25,
                "pikachu",
                112,
                4,
                true,
                35,
                60
            )
        )
    }

    @Test
    fun `name이 null이면 공란으로 변환된다`() {
        assertThat(dto(name = null).toModel().name).isBlank()
    }

    @Test
    fun `name이 빈 문자열이면 공란으로 변환된다`() {
        assertThat(dto(name = "").toModel().name).isBlank()
    }

    @Test
    fun `name이 공백뿐이면 공란으로 변환된다`() {
        assertThat(dto(name = "   ").toModel().name).isBlank()
    }

    @Test
    fun `숫자 필드가 null이면 0으로 변환된다`() {
        val model = dto(
            id = null,
            baseExperience = null,
            height = null,
            order = null,
            weight = null
        ).toModel()

        assertThat(model.id).isEqualTo(0)
        assertThat(model.baseExperience).isEqualTo(0)
        assertThat(model.height).isEqualTo(0)
        assertThat(model.order).isEqualTo(0)
        assertThat(model.weight).isEqualTo(0)
    }

    @Test
    fun `isDefault가 null이면 false로 변환된다`() {
        assertThat(dto(isDefault = null).toModel().isDefault).isFalse()
    }

    @Test
    fun `모든 필드가 null이어도 예외 없이 기본값 Model이 만들어진다`() {
        val allNull = PokemonDto(
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )

        assertThatCode { allNull.toModel() }.doesNotThrowAnyException()
        assertThat(allNull.toModel()).isEqualTo(
            Pokemon(
                0,
                "",
                0,
                0,
                false,
                0,
                0
            )
        )
    }

    @Test
    fun `유효한 값은 기본값 처리 중에도 유지된다`() {
        val model = dto(name = "", height = null).toModel()

        assertThat(model.id).isEqualTo(25)
        assertThat(model.weight).isEqualTo(60)
        assertThat(model.name).isEqualTo("")
        assertThat(model.height).isEqualTo(0)
    }

    @Test
    fun `Model을 DTO로 변환하면 모든 필드가 그대로 옮겨진다`() {
        val pokemon = Pokemon(
            25,
            "pikachu",
            112,
            4,
            true,
            35,
            60
        )

        val dto = pokemon.toDto()

        assertThat(dto.id).isEqualTo(25)
        assertThat(dto.name).isEqualTo("pikachu")
        assertThat(dto.baseExperience).isEqualTo(112)
        assertThat(dto.height).isEqualTo(4)
        assertThat(dto.isDefault).isTrue()
        assertThat(dto.order).isEqualTo(35)
        assertThat(dto.weight).isEqualTo(60)
    }

    @Test
    fun `유효한 Model은 DTO를 거쳐 다시 변환해도 동일하다`() {
        val pokemon = Pokemon(
            25,
            "pikachu",
            112,
            4,
            true,
            35,
            60
        )

        assertThat(pokemon.toDto().toModel()).isEqualTo(pokemon)
    }
}
