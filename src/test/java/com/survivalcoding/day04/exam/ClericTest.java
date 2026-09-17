package com.survivalcoding.day04.exam;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClericTest {

    @Test
    @DisplayName("heal을 하면 hp를 10 회복해야 한다")
    void heal() {
        // Given
        final Cleric cleric = new Cleric();

        // mp소비 코스트보다 높은 값
        final int enoughMp = cleric.costForSelfAid;
        cleric.mp = enoughMp;
        final int beforeMp = cleric.mp;

        // When
        cleric.selfAid();

        // Then
        assertEquals(cleric.MAX_HP, cleric.hp);
        assertEquals(beforeMp - cleric.costForSelfAid, cleric.mp);
    }

    @Test
    @DisplayName("MP가 부족하면 회복을 못 한다")
    void heal2() {
        // Given
        final Cleric cleric = new Cleric();

        // 스킬 사용 불가한 costForSelfAid - 1 로 설정
        final int notEnoughMp = cleric.costForSelfAid - 1;
        cleric.mp = notEnoughMp;
        final int beforeHp = cleric.hp;
        final int beforeMp = cleric.mp;

        // When
        cleric.selfAid();

        // Then
        assertEquals(beforeHp, cleric.hp);
        assertEquals(beforeMp, cleric.mp);
    }

    @Test
    @DisplayName("회복 마나가 최댓값보다 작을 때")
    void restoreMp() {
        // Given
        final Cleric cleric = new Cleric();

        // 최대값보다 작은 값
        final int notMaxMp = cleric.MAX_MP - 1;
        cleric.mp = notMaxMp;
        final int beforeMp = cleric.mp;
        final int durationSecond = 9;

        final int minMpRestoreAmount = beforeMp + durationSecond;
        final int maxMpRestoreAmount = minMpRestoreAmount + cleric.maxCorrectionValue;

        // When
        final int mpRestoreAmount = cleric.pray(durationSecond);
        boolean isInRange = minMpRestoreAmount <= cleric.mp && cleric.mp <= maxMpRestoreAmount;

        // Then
        assertTrue(isInRange);
        assertEquals(beforeMp + mpRestoreAmount, cleric.mp);
    }

    @Test
    @DisplayName("회복 마나가 maxMp를 넘길 때")
    void restoreMp2() {
        // Given
        final Cleric cleric = new Cleric();

        // mp 최대값에 가깝게
        final int mpNearMax = cleric.MAX_MP - 1;
        cleric.mp = mpNearMax;
        final int beforeMp = cleric.mp;
        final int durationSecond = 3;

        // When
        final int mpRestoreAmount = cleric.pray(durationSecond);

        // Then
        assertEquals(cleric.MAX_MP, beforeMp + mpRestoreAmount);
        assertEquals(cleric.MAX_MP, cleric.mp);
    }

    @Test
    @DisplayName("잘못된 기도 시간")
    void restoreMp3() {
        // Given
        final Cleric cleric = new Cleric();
        final int invalidDurationSecond = -1;
        final int invalidDurationErrCode = -1;
        final int beforeMp = cleric.mp;

        // When
        final int mpRestoreAmount = cleric.pray(invalidDurationSecond);

        // Then
        assertEquals(invalidDurationErrCode, mpRestoreAmount);
        assertEquals(beforeMp, cleric.mp);
    }

    @Test
    @DisplayName("최대 마나면 회복 불가")
    void restoreMp4() {
        // Given
        final Cleric cleric = new Cleric();
        cleric.mp = cleric.MAX_MP;
        final int durationSecond = 5;
        final int maxMpCode = 0;
        final int beforeMp = cleric.mp;

        // When
        final int mpRestoreAmount = cleric.pray(durationSecond);

        // Then
        assertEquals(maxMpCode, mpRestoreAmount);
        assertEquals(beforeMp, cleric.mp);
    }
}