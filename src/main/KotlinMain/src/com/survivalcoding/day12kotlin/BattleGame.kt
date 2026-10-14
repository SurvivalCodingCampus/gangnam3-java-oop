package com.survivalcoding.day12kotlin

class BattleGame {
    fun play(
        party: List<Character>,
        opponents: List<Monster>,
    ): List<BattleEvent> {
        require(party.isNotEmpty()) { "파티는 비어 있을 수 없습니다." }
        require(opponents.isNotEmpty()) { "상대 목록은 비어 있을 수 없습니다." }
        require(party.all { it is Attackable }) { "모든 파티원은 공격할 수 있어야 합니다." }

        val events = mutableListOf<BattleEvent>()
        var round = 1
        var totalExp = 0

        while (party.any(Character::isAlive) && opponents.any(Character::isAlive)) {
            val livingHpAtRoundStart =
                party.filter(Character::isAlive).sumOf(Character::hp) +
                    opponents.filter(Character::isAlive).sumOf(Character::hp)
            events += BattleEvent.RoundStarted(round++)

            val activeOpponent = opponents.first(Character::isAlive)
            val defendingPartyMember = party.first(Character::isAlive)
            events += BattleEvent.AttackPerformed(activeOpponent.attack(defendingPartyMember))

            if (party.none(Character::isAlive)) {
                events += BattleEvent.PartyDefeat
                return events
            }

            for (member in party.filter(Character::isAlive)) {
                val target = opponents.firstOrNull(Character::isAlive) ?: break
                val result = when (member) {
                    is Wizard -> if (member.mp >= Wizard.MAGIC_COST) {
                        member.castMagic(target)
                    } else {
                        member.attack(target)
                    }
                    else -> (member as Attackable).attack(target)
                }
                events += BattleEvent.AttackPerformed(result)

                if (!target.isAlive) {
                    val reward = (target as? AdvancedMonster)?.rewardExp ?: 0
                    totalExp += reward
                    events += BattleEvent.OpponentDefeated(target.name, reward)
                }
            }

            val battleIsOngoing =
                party.any(Character::isAlive) && opponents.any(Character::isAlive)
            val livingHpAfterRound =
                party.filter(Character::isAlive).sumOf(Character::hp) +
                    opponents.filter(Character::isAlive).sumOf(Character::hp)
            check(!battleIsOngoing || livingHpAfterRound != livingHpAtRoundStart) {
                "전투가 교착 상태입니다. 이번 라운드에 HP 변화가 없습니다."
            }
        }

        events += if (opponents.none(Character::isAlive)) {
            BattleEvent.PartyVictory(totalExp)
        } else {
            BattleEvent.PartyDefeat
        }
        return events
    }
}
