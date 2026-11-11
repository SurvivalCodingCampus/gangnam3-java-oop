package com.survivalcoding.`18day`.pattern.factory

import com.survivalcoding.`18day`.model.Pokemon

/**
 * [GoF Builder Pattern] 포켓몬 도메인 모델 빌더.
 *
 * Pokemon 데이터 클래스는 생성자 매개변수가 많아 객체 생성 시 가독성이 떨어질 수 있습니다.
 * Builder를 사용하면 필요한 필드만 선택적으로 설정하여 읽기 쉽고 실수 없는 객체 생성이 가능합니다.
 *
 * 예:
 *   val p = PokemonBuilder { name = "pikachu"; type = "electric"; hp = 35 }.build()
 */
class PokemonBuilder private constructor(
    var id: Int = 0,
    var name: String = "",
    var type: String = "unknown",
    var height: Int = 0,
    var weight: Int = 0,
    var baseExperience: Int = 0,
    var hp: Int = 1,
    var attack: Int = 0,
    var defense: Int = 0,
    var speed: Int = 0,
    var specialAttack: Int = 0,
    var specialDefense: Int = 0,
    var imageUrl: String? = null,
    var soundUrl: String? = null,
    var abilities: String = ""
) {

    constructor(init: PokemonBuilder.() -> Unit) : this() {
        init(this)
    }

    fun build(): Pokemon {
        return Pokemon(
            id = id,
            name = name.takeIf { it.isNotBlank() } ?: "알 수 없는 포켓몬",
            type = type.takeIf { it.isNotBlank() } ?: "unknown",
            height = height.coerceAtLeast(0),
            weight = weight.coerceAtLeast(0),
            baseExperience = baseExperience.coerceAtLeast(0),
            hp = hp.coerceAtLeast(1),
            attack = attack.coerceAtLeast(0),
            defense = defense.coerceAtLeast(0),
            speed = speed.coerceAtLeast(0),
            specialAttack = specialAttack.coerceAtLeast(0),
            specialDefense = specialDefense.coerceAtLeast(0),
            imageUrl = imageUrl?.takeIf { it.isNotBlank() },
            soundUrl = soundUrl?.takeIf { it.isNotBlank() },
            abilities = abilities
        )
    }
}
