package com.survivalcoding.day04.exam;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClericTest {

    @Test
    @DisplayName("heal을 하면 hp를 10 회복해야 한다")
    void heal() {
        // Given
        final Cleric cleric = new Cleric("엄");

        // mp소비 코스트보다 높은 값
        final int enoughMp = Cleric.COST_FOR_SELF_AID;
        cleric.mp = enoughMp;
        final int beforeMp = cleric.mp;

        // When
        cleric.selfAid();

        // Then
        assertEquals(Cleric.MAX_HP, cleric.hp);
        assertEquals(beforeMp - Cleric.COST_FOR_SELF_AID, cleric.mp);
    }

    @Test
    @DisplayName("MP가 부족하면 회복을 못 한다")
    void heal2() {
        // Given
        final Cleric cleric = new Cleric("엄");

        // 스킬 사용 불가한 costForSelfAid - 1 로 설정
        final int notEnoughMp = Cleric.COST_FOR_SELF_AID - 1;
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
        final Cleric cleric = new Cleric("엄");

        // 최대값보다 작은 값
        final int minMp = 0;

        cleric.mp = minMp;
        final int beforeMp = cleric.mp;
        final int durationSecond = 3;

        final int minMpRestoreAmount = beforeMp + durationSecond;
        final int maxMpRestoreAmount = minMpRestoreAmount + Cleric.MAX_CORRECTION_VALUE;

        System.out.println(minMpRestoreAmount);
        System.out.println(maxMpRestoreAmount);
        // When
        final int mpRestoreAmount = cleric.pray(durationSecond);
        boolean isInRange = minMpRestoreAmount <= cleric.mp && cleric.mp <= maxMpRestoreAmount;


        System.out.println(cleric.mp);
        // Then
        assertTrue(isInRange);
        assertEquals(beforeMp + mpRestoreAmount, cleric.mp);
    }

    @Test
    @DisplayName("회복 마나가 maxMp를 넘길 때")
    void restoreMp2() {
        // Given
        final Cleric cleric = new Cleric("엄");

        // mp 최대값에 가깝게
        final int mpNearMax = cleric.MAX_MP - 1;
        cleric.mp = mpNearMax;
        final int beforeMp = cleric.mp;
        final int durationSecond = 3;

        // When
        final int mpRestoreAmount = cleric.pray(durationSecond);

        // Then
        assertEquals(Cleric.MAX_MP, beforeMp + mpRestoreAmount);
        assertEquals(Cleric.MAX_MP, cleric.mp);
    }

    @Test
    @DisplayName("잘못된 기도 시간")
    void restoreMp3() {
        // Given
        final int minMp = 0;
        final Cleric cleric = new Cleric("엄");
        cleric.mp = minMp;
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
        final Cleric cleric = new Cleric("엄");
        cleric.mp = Cleric.MAX_MP;
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

@DisplayName("Day05 테스트(생성자)")
class ClericTestDay05 {
    @Test
    @DisplayName("여러 Cleric 인스턴스는 MAX_HP와 MAX_MP 값을 공유한다")
    void maxHpAndMaxMpShouldBeShared() {
        // given
        String tmpName = "홍길동";
        final Cleric cleric1 = new Cleric(tmpName);
        final Cleric cleric2 = new Cleric(tmpName);

        // when & then
        assertEquals(Cleric.MAX_HP, cleric1.MAX_HP);
        assertEquals(Cleric.MAX_HP, cleric2.MAX_HP);

        assertEquals(Cleric.MAX_MP, cleric1.MAX_MP);
        assertEquals(Cleric.MAX_MP, cleric2.MAX_MP);
    }

    @Test
    @DisplayName("이름만 입력하면 HP와 MP는 최대값으로 초기화된다")
    void constructorWithNameShouldSetMaxHpAndMp() {
        // given & when
        final Cleric cleric = new Cleric("홍길동");

        // then
        assertEquals("홍길동", cleric.name);
        assertEquals(Cleric.MAX_HP, cleric.hp);
        assertEquals(Cleric.MAX_MP, cleric.mp);
    }

    @Test
    @DisplayName("이름과 HP만 입력하면 MP는 최대값으로 초기화된다")
    void constructorWithNameAndHpShouldSetMaxMp() {
        // given
        int hp = 30;

        // when
        final Cleric cleric = new Cleric("홍길동", hp);

        // then
        assertEquals("홍길동", cleric.name);
        assertEquals(hp, cleric.hp);
        assertEquals(Cleric.MAX_MP, cleric.mp);
    }

    @Test
    @DisplayName("이름이 null이면 정상적인 이름으로 저장되지 않는다")
    void nameShouldNotAcceptNull() {
        // when
        final Cleric cleric = new Cleric(null);

        // then
        assertNull(cleric.name);
    }

    @Test
    @DisplayName("이름이 빈 문자열이면 정상적인 이름으로 저장되지 않는다")
    void nameShouldNotAcceptEmptyString() {
        // when
        final Cleric cleric = new Cleric("");

        // then
        assertNull(cleric.name);
    }

    @Test
    @DisplayName("이름이 공백 한 칸이면 정상적인 이름으로 저장되지 않는다")
    void nameShouldNotAcceptSingleBlank() {
        // when
        final Cleric cleric = new Cleric(" ");

        // then
        assertNull(cleric.name);
    }

    @Test
    @DisplayName("이름이 여러 공백이면 정상적인 이름으로 저장되지 않는다")
    void nameShouldNotAcceptMultipleBlanks() {
        // when
        final Cleric cleric = new Cleric("     ");

        // then
        assertNull(cleric.name);
    }

    @Test
    @DisplayName("일반적인 이름은 정상적으로 저장된다 - 동등 분할")
    void validNameShouldBeStored() {
        // given
        final String name = "홍길동";

        // when
        final Cleric cleric = new Cleric(name);

        // then
        assertEquals(name, cleric.name);
    }

    @Test
    @DisplayName("HP 최소 정상값 1은 저장된다")
    void hpMinimumBoundary() {
        // when
        final Cleric cleric = new Cleric("홍길동", 1);

        // then
        assertEquals(1, cleric.hp);
    }

    @Test
    @DisplayName("HP 0은 저장되지 않는다")
    void hpZeroShouldNotBeAccepted() {
        // when
        final Cleric cleric = new Cleric("홍길동", 0);

        // then
        assertEquals(0, cleric.hp);
    }

    @Test
    @DisplayName("HP 음수는 저장되지 않는다")
    void negativeHpShouldNotBeAccepted() {
        // when
        final Cleric cleric = new Cleric("홍길동", -1);

        // then
        assertEquals(0, cleric.hp);
    }

    @Test
    @DisplayName("HP 최대값 바로 아래인 49는 저장된다")
    void hpJustBelowMaximum() {
        // when
        final Cleric cleric
                = new Cleric("홍길동", Cleric.MAX_HP - 1);

        // then
        assertEquals(49, cleric.hp);
    }

    @Test
    @DisplayName("HP 최대값 50을 입력할 수 있다")
    void hpMaximumBoundary() {
        // when
        final Cleric cleric
                = new Cleric("홍길동", Cleric.MAX_HP);

        // then
        assertEquals(Cleric.MAX_HP, cleric.hp);
    }

    @Test
    @DisplayName("HP 최대값보다 큰 값은 저장되지 않는다")
    void hpOverMaximumShouldNotBeAccepted() {
        // when
        final Cleric cleric
                = new Cleric("홍길동", Cleric.MAX_HP + 1);

        // then
        assertNotEquals(Cleric.MAX_HP + 1, cleric.hp);
    }

    @Test
    @DisplayName("MP 최소 정상값 1은 저장된다")
    void mpMinimumBoundary() {
        // when
        final Cleric cleric
                = new Cleric("홍길동", 30, 1);

        // then
        assertEquals(1, cleric.mp);
    }

    @Test
    @DisplayName("MP 0은 저장되지 않는다")
    void mpZeroShouldNotBeAccepted() {
        // when
        final Cleric cleric
                = new Cleric("홍길동", 30, 0);

        // then
        assertEquals(0, cleric.mp);
    }

    @Test
    @DisplayName("MP 음수는 저장되지 않는다")
    void negativeMpShouldNotBeAccepted() {
        // when
        final Cleric cleric
                = new Cleric("홍길동", 30, -1);

        // then
        assertEquals(0, cleric.mp);
    }

    @Test
    @DisplayName("MP 최대값 바로 아래인 9는 저장된다")
    void mpJustBelowMaximum() {
        // when
        final Cleric cleric
                = new Cleric("홍길동", 30, Cleric.MAX_MP - 1);

        // then
        assertEquals(9, cleric.mp);
    }

    @Test
    @DisplayName("MP 최대값 10을 입력할 수 있다")
    void mpMaximumBoundary() {
        // when
        final Cleric cleric
                = new Cleric("홍길동", 30, Cleric.MAX_MP);

        // then
        assertEquals(Cleric.MAX_MP, cleric.mp);
    }

    @Test
    @DisplayName("MP 최대값보다 큰 값은 저장되지 않는다")
    void mpOverMaximumShouldNotBeAccepted() {
        // when
        final Cleric cleric
                = new Cleric("홍길동", 30, Cleric.MAX_MP + 1);

        // then
        assertNotEquals(Cleric.MAX_MP + 1, cleric.mp);
    }
}