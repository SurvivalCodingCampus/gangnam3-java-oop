package com.survivalcoding.`18day`.test

import com.survivalcoding.`18day`.battle.DefaultTypeEffectivenessStrategy
import com.survivalcoding.`18day`.battle.PokemonBattleService
import com.survivalcoding.`18day`.model.Pokemon

/**
 * 포켓몬 배틀 로직 테스트
 *
 * 테스트 시나리오:
 * 1. calculateDamage: 기본 데미지 계산 (공격력 - 방어력/2, 최소 1)
 * 2. calculateDamage: 타입 상성 배율 적용 (불→풀 2.0, 물→불 2.0 등)
 * 3. calculateDamage: 상성 저항 (불→불 0.5, 물→물 0.5 등)
 * 4. takeDamage: HP 감소, 0 이하로 내려가지 않음
 * 5. takeDamage: 복사본 반환, 원본은 불변
 * 6. isAlive: HP > 0 이면 true, HP == 0 이면 false
 * 7. attackMessage: 공격 메시지 형식 및 상성 메시지 포함
 * 8. hasImage: 이미지 URL 존재 여부
 * 9. hasSoundFile: 사운드 파일 존재 여부 (실제 파일 없으면 false)
 * 10. getAvailableSoundUrl: 실제 파일 있으면 URL 반환, 없으면 null
 */
class PokemonBattleTest {

    // 배틀 서비스 (SRP: 배틀 로직 전담)
    private val battleService = PokemonBattleService(DefaultTypeEffectivenessStrategy())

    // ============================================================
    // 테스트 실행 메서드 (main)
    // ============================================================

    fun main() {
        println("========== [PokemonBattleTest 시작] ==========")
        println()

        var passCount = 0
        var failCount = 0

        val results = mutableListOf<Pair<String, Boolean>>()

        results += testCalculateDamageBasic()
        results += testCalculateDamageTypeEffectiveness()
        results += testCalculateDamageResistance()
        results += testCalculateDamageMinimum1()
        results += testTakeDamageReducesHP()
        results += testTakeDamageNotBelowZero()
        results += testTakeDamageReturnsCopy()
        results += testIsAlive()
        results += testAttackMessageSuperEffective()
        results += testAttackMessageNotVeryEffective()
        results += testAttackMessageNormal()
        results += testHasImage()
        results += testHasSoundFileNoFile()
        results += testGetAvailableSoundUrlNoFile()

        println()
        println("========== [테스트 결과 요약] ==========")
        for ((name, passed) in results) {
            val status = if (passed) "✅ 통과" else "❌ 실패"
            println("  $status | $name")
            if (passed) passCount++ else failCount++
        }
        println()
        println("총 테스트: ${results.size}, 통과: $passCount, 실패: $failCount")
        println()
        println("========== [PokemonBattleTest 종료] ==========")
    }

    // ============================================================
    // 개별 테스트 케이스
    // ============================================================

    /**
     * 시나리오 1: calculateDamage - 기본 데미지 계산
     * 공격력 50, 방어력 20 → 50 - 20/2 = 40
     */
    private fun testCalculateDamageBasic(): Pair<String, Boolean> {
        val attacker = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 35, attack = 50, defense = 20)
        val defender = Pokemon(id = 6, name = "charizard", type = "fire", height = 17, weight = 905,
            baseExperience = 240, hp = 78, attack = 84, defense = 20)

        val damage = battleService.calculateDamage(attacker, defender)
        val passed = damage == 40

        println("[테스트 1] 기본 데미지 계산: ${if (passed) "통과" else "실패"} (예상: 40, 실제: $damage)")
        return "기본 데미지 계산" to passed
    }

    /**
     * 시나리오 2: calculateDamage - 타입 상성 배율 (불→풀 = 2.0)
     */
    private fun testCalculateDamageTypeEffectiveness(): Pair<String, Boolean> {
        val attacker = Pokemon(id = 6, name = "charizard", type = "fire", height = 17, weight = 905,
            baseExperience = 240, hp = 78, attack = 60, defense = 30)
        val defender = Pokemon(id = 1, name = "bulbasaur", type = "grass", height = 7, weight = 69,
            baseExperience = 64, hp = 45, attack = 49, defense = 20)

        // baseDamage = 60 - 20/2 = 50, effectiveness = 2.0 → 100
        val damage = battleService.calculateDamage(attacker, defender)
        val passed = damage == 100

        println("[테스트 2] 타입 상성 배율 (불→풀): ${if (passed) "통과" else "실패"} (예상: 100, 실제: $damage)")
        return "타입 상성 배율" to passed
    }

    /**
     * 시나리오 3: calculateDamage - 상성 저항 (불→불 = 0.5)
     */
    private fun testCalculateDamageResistance(): Pair<String, Boolean> {
        val attacker = Pokemon(id = 6, name = "charizard", type = "fire", height = 17, weight = 905,
            baseExperience = 240, hp = 78, attack = 60, defense = 30)
        val defender = Pokemon(id = 6, name = "charizard", type = "fire", height = 17, weight = 905,
            baseExperience = 240, hp = 78, attack = 84, defense = 30)

        // baseDamage = 60 - 30/2 = 45, effectiveness = 0.5 → 22 (소수점 버림)
        val damage = battleService.calculateDamage(attacker, defender)
        val passed = damage == 22

        println("[테스트 3] 타입 상성 저항 (불→불): ${if (passed) "통과" else "실패"} (예상: 22, 실제: $damage)")
        return "타입 상성 저항" to passed
    }

    /**
     * 시나리오 4: calculateDamage - 최소 데미지 1 보장
     * 방어력이 매우 높아서 baseDamage가 0 이하가 되어도 최소 1
     */
    private fun testCalculateDamageMinimum1(): Pair<String, Boolean> {
        val attacker = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 35, attack = 5, defense = 100)
        val defender = Pokemon(id = 143, name = "snorlax", type = "normal", height = 21, weight = 4600,
            baseExperience = 154, hp = 160, attack = 110, defense = 100)

        // baseDamage = 5 - 100/2 = -45 → 코어에서 1로 보정, effectiveness = 1.0 → 1
        val damage = battleService.calculateDamage(attacker, defender)
        val passed = damage == 1

        println("[테스트 4] 최소 데미지 1 보장: ${if (passed) "통과" else "실패"} (예상: 1, 실제: $damage)")
        return "최소 데미지 1" to passed
    }

    /**
     * 시나리오 5: takeDamage - HP 감소
     */
    private fun testTakeDamageReducesHP(): Pair<String, Boolean> {
        val pokemon = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 35, attack = 55, defense = 40)

        val damaged = battleService.applyDamage(pokemon, 10)
        val passed = damaged.hp == 25

        println("[테스트 5] HP 감소: ${if (passed) "통과" else "실패"} (예상: 25, 실제: ${damaged.hp})")
        return "HP 감소" to passed
    }

    /**
     * 시나리오 6: takeDamage - HP 0 이하로 내려가지 않음
     */
    private fun testTakeDamageNotBelowZero(): Pair<String, Boolean> {
        val pokemon = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 10, attack = 55, defense = 40)

        val damaged = battleService.applyDamage(pokemon, 100)
        val passed = damaged.hp == 0

        println("[테스트 6] HP 0 이하 방지: ${if (passed) "통과" else "실패"} (예상: 0, 실제: ${damaged.hp})")
        return "HP 0 이하 방지" to passed
    }

    /**
     * 시나리오 7: takeDamage - 원본 불변, 복사본 반환
     */
    private fun testTakeDamageReturnsCopy(): Pair<String, Boolean> {
        val pokemon = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 35, attack = 55, defense = 40)

        val damaged = battleService.applyDamage(pokemon, 10)
        val passed = pokemon.hp == 35 && damaged.hp == 25 && pokemon !== damaged

        println("[테스트 7] 원본 불변 / 복사본 반환: ${if (passed) "통과" else "실패"} " +
            "(원본 HP: ${pokemon.hp}, 복사본 HP: ${damaged.hp})")
        return "원본 불변" to passed
    }

    /**
     * 시나리오 8: isAlive - HP > 0 이면 true
     */
    private fun testIsAlive(): Pair<String, Boolean> {
        val alive = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 35, attack = 55, defense = 40)
        val fainted = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 0, attack = 55, defense = 40)

        val passed = alive.isAlive() && !fainted.isAlive()

        println("[테스트 8] isAlive: ${if (passed) "통과" else "실패"} " +
            "(HP=35: ${alive.isAlive()}, HP=0: ${fainted.isAlive()})")
        return "isAlive" to passed
    }

    /**
     * 시나리오 9: attackMessage - 효과가 탁월한 경우 (배율 > 1.0)
     */
    private fun testAttackMessageSuperEffective(): Pair<String, Boolean> {
        val attacker = Pokemon(id = 6, name = "charizard", type = "fire", height = 17, weight = 905,
            baseExperience = 240, hp = 78, attack = 60, defense = 30)
        val defender = Pokemon(id = 1, name = "bulbasaur", type = "grass", height = 7, weight = 69,
            baseExperience = 64, hp = 45, attack = 49, defense = 20)

        val msg = battleService.createAttackMessage(attacker, defender, 100, 2.0)
        val passed = msg.contains("공격!") && msg.contains("100 데미지") && msg.contains("탁월하다")

        println("[테스트 9] 공격 메시지 (효과 탁월): ${if (passed) "통과" else "실패"}")
        if (!passed) println("  실제 메시지: $msg")
        return "공격 메시지 - 탁월" to passed
    }

    /**
     * 시나리오 10: attackMessage - 효과가 별로 없는 경우 (0 < 배율 < 1.0)
     */
    private fun testAttackMessageNotVeryEffective(): Pair<String, Boolean> {
        val attacker = Pokemon(id = 6, name = "charizard", type = "fire", height = 17, weight = 905,
            baseExperience = 240, hp = 78, attack = 60, defense = 30)
        val defender = Pokemon(id = 6, name = "charizard", type = "fire", height = 17, weight = 905,
            baseExperience = 240, hp = 78, attack = 84, defense = 30)

        val msg = battleService.createAttackMessage(attacker, defender, 22, 0.5)
        val passed = msg.contains("별로 없는 것 같다")

        println("[테스트 10] 공격 메시지 (효과 낮음): ${if (passed) "통과" else "실패"}")
        if (!passed) println("  실제 메시지: $msg")
        return "공격 메시지 - 낮음" to passed
    }

    /**
     * 시나리오 11: attackMessage - 일반 상성 (배율 = 1.0)
     */
    private fun testAttackMessageNormal(): Pair<String, Boolean> {
        val attacker = Pokemon(id = 52, name = "meowth", type = "normal", height = 4, weight = 42,
            baseExperience = 69, hp = 40, attack = 45, defense = 35)
        val defender = Pokemon(id = 54, name = "psyduck", type = "water", height = 8, weight = 196,
            baseExperience = 64, hp = 50, attack = 52, defense = 48)

        val msg = battleService.createAttackMessage(attacker, defender, 20, 1.0)
        val passed = msg.contains("공격!") && msg.contains("20 데미지") && !msg.contains("탁월하다") &&
            !msg.contains("별로 없는 것 같다")

        println("[테스트 11] 공격 메시지 (일반): ${if (passed) "통과" else "실패"}")
        if (!passed) println("  실제 메시지: $msg")
        return "공격 메시지 - 일반" to passed
    }

    /**
     * 시나리오 12: hasImage - 이미지 URL이 있으면 true
     */
    private fun testHasImage(): Pair<String, Boolean> {
        val withImage = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 35, attack = 55, defense = 40,
            imageUrl = "https://example.com/pikachu.png")
        val withoutImage = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 35, attack = 55, defense = 40, imageUrl = null)

        val passed = withImage.hasImage() && !withoutImage.hasImage()

        println("[테스트 12] hasImage: ${if (passed) "통과" else "실패"} " +
            "(URL 있음: ${withImage.hasImage()}, null: ${withoutImage.hasImage()})")
        return "hasImage" to passed
    }

    /**
     * 시나리오 13: hasSoundFile - 실제 파일이 없으면 false
     */
    private fun testHasSoundFileNoFile(): Pair<String, Boolean> {
        val pokemon = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 35, attack = 55, defense = 40,
            soundUrl = "sounds/nonexistent.wav")

        val passed = !pokemon.hasSoundFile()

        println("[테스트 13] hasSoundFile (파일 없음): ${if (passed) "통과" else "실패"} (예상: false)")
        return "hasSoundFile - 파일 없음" to passed
    }

    /**
     * 시나리오 14: getAvailableSoundUrl - 실제 파일이 없으면 null
     */
    private fun testGetAvailableSoundUrlNoFile(): Pair<String, Boolean> {
        val pokemon = Pokemon(id = 25, name = "pikachu", type = "electric", height = 4, weight = 60,
            baseExperience = 112, hp = 35, attack = 55, defense = 40,
            soundUrl = "sounds/nonexistent.wav")

        val passed = pokemon.getAvailableSoundUrl() == null

        println("[테스트 14] getAvailableSoundUrl (파일 없음): ${if (passed) "통과" else "실패"} (예상: null)")
        return "getAvailableSoundUrl - 파일 없음" to passed
    }
}

// ============================================================
// 테스트 실행 진입점
// ============================================================

fun main() {
    PokemonBattleTest().main()
}
