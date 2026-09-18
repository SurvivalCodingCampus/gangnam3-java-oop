package com.survivalcoding.day01_class_instance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClericTest {
    @RepeatedTest(20)
    @DisplayName("시나리오 1: pray를 3초 하면 3~5가 회복되어야 함")
    void pray_threeSeconds_recoversBetween3And5() {
        Cleric cleric = new Cleric();
        cleric.mp = 0; // 최대치 도달에 따른 제한을 피하기 위해 0으로 세팅

        int recoveredMp = cleric.pray(3);

        // 3초 + (0~2) 보정 = 회복량은 3 이상 5 이하
        assertTrue(recoveredMp >= 3 && recoveredMp <= 5,
                "회복량은 3~5 사이여야 합니다. 실제 회복량: " + recoveredMp);
        assertEquals(recoveredMp, cleric.mp, "실제 mp 필드에도 회복량이 정확히 반영되어야 합니다.");
    }

    @Test
    @DisplayName("시나리오 2: mp가 5일 때 selfAid를 하면 hp가 maxHp가 되어야 함")
    void selfAid_whenMpIs5_recoversHpToMax() {
        Cleric cleric = new Cleric();
        cleric.hp = 10; // 체력이 깎인 상태
        cleric.mp = 5;  // 정확히 소모량만큼 남은 상태

        cleric.selfAid();

        assertEquals(cleric.maxHp, cleric.hp, "hp가 maxHp로 회복되어야 합니다.");
        assertEquals(0, cleric.mp, "mp는 5가 소모되어 0이 되어야 합니다.");
    }
}