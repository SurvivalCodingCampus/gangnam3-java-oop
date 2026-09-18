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

@DisplayName("생성 테스트")
class ClericTestDay05 {

    @Test
    @DisplayName("이름만 입력하면 HP와 MP는 최대값으로 초기화된다")
    void createWithNameShouldSetMaxHpAndMp() {
        // given
        final String name = "홍길동";

        // when
        final Cleric cleric = Cleric.CreateOrNull(name);

        // then
        assertNotNull(cleric);
        assertEquals(name, cleric.name);
        assertEquals(Cleric.MAX_HP, cleric.hp);
        assertEquals(Cleric.MAX_MP, cleric.mp);
    }

    @Test
    @DisplayName("이름과 HP만 입력하면 MP는 최대값으로 초기화된다")
    void createWithNameAndHpShouldSetMaxMp() {
        // given
        final String name = "홍길동";
        final int hp = 30;

        // when
        final Cleric cleric = Cleric.CreateOrNull(name, hp);

        // then
        assertNotNull(cleric);
        assertEquals(name, cleric.name);
        assertEquals(hp, cleric.hp);
        assertEquals(Cleric.MAX_MP, cleric.mp);
    }

    @Test
    @DisplayName("이름, HP, MP를 입력하면 해당 값으로 초기화된다")
    void createWithNameHpAndMpShouldSetAllValues() {
        // given
        final String name = "홍길동";
        final int hp = 30;
        final int mp = 5;

        // when
        final Cleric cleric = Cleric.CreateOrNull(name, hp, mp);

        // then
        assertNotNull(cleric);
        assertEquals(name, cleric.name);
        assertEquals(hp, cleric.hp);
        assertEquals(mp, cleric.mp);
    }

    // --------------------------------------------------
    // 이름 테스트
    // --------------------------------------------------

    @Test
    @DisplayName("이름이 null이면 Cleric을 생성하지 않는다")
    void nameShouldNotAcceptNull() {
        // when
        final Cleric cleric = Cleric.CreateOrNull(null);

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("이름이 빈 문자열이면 Cleric을 생성하지 않는다")
    void nameShouldNotAcceptEmptyString() {
        // when
        final Cleric cleric = Cleric.CreateOrNull("");

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("이름이 공백 한 칸이면 Cleric을 생성하지 않는다")
    void nameShouldNotAcceptSingleBlank() {
        // when
        final Cleric cleric = Cleric.CreateOrNull(" ");

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("이름이 여러 공백이면 Cleric을 생성하지 않는다")
    void nameShouldNotAcceptMultipleBlanks() {
        // when
        final Cleric cleric = Cleric.CreateOrNull("     ");

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("일반적인 이름은 정상적으로 저장된다 - 동등 분할")
    void validNameShouldBeStored() {
        // given
        final String name = "홍길동";

        // when
        final Cleric cleric = Cleric.CreateOrNull(name);

        // then
        assertNotNull(cleric);
        assertEquals(name, cleric.name);
    }

    // --------------------------------------------------
    // HP 테스트
    // 현재 허용 범위: 0 ~ MAX_HP
    // --------------------------------------------------

    @Test
    @DisplayName("HP 최소값 0은 저장된다")
    void hpMinimumBoundary() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MIN_HP);

        // then
        assertNotNull(cleric);
        assertEquals(Cleric.MIN_HP, cleric.hp);
    }

    @Test
    @DisplayName("HP 최소값보다 작은 값은 허용되지 않는다")
    void hpBelowMinimumShouldNotBeAccepted() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MIN_HP - 1);

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("HP 최소값 바로 위인 1은 저장된다")
    void hpJustAboveMinimum() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MIN_HP + 1);

        // then
        assertNotNull(cleric);
        assertEquals(1, cleric.hp);
    }

    @Test
    @DisplayName("HP 최대값 바로 아래 값은 저장된다")
    void hpJustBelowMaximum() {
        // given
        final int hp = Cleric.MAX_HP - 1;

        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", hp);

        // then
        assertNotNull(cleric);
        assertEquals(hp, cleric.hp);
    }

    @Test
    @DisplayName("HP 최대값은 저장된다")
    void hpMaximumBoundary() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MAX_HP);

        // then
        assertNotNull(cleric);
        assertEquals(Cleric.MAX_HP, cleric.hp);
    }

    @Test
    @DisplayName("HP 최대값보다 큰 값은 허용되지 않는다")
    void hpOverMaximumShouldNotBeAccepted() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MAX_HP + 1);

        // then
        assertNull(cleric);
    }

    // --------------------------------------------------
    // MP 테스트
    // 현재 허용 범위: 0 ~ MAX_MP
    // --------------------------------------------------

    @Test
    @DisplayName("MP 최소값 0은 저장된다")
    void mpMinimumBoundary() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", 30, Cleric.MIN_MP);

        // then
        assertNotNull(cleric);
        assertEquals(Cleric.MIN_MP, cleric.mp);
    }

    @Test
    @DisplayName("MP 최소값보다 작은 값은 허용되지 않는다")
    void mpBelowMinimumShouldNotBeAccepted() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", 30, Cleric.MIN_MP - 1);

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("MP 최소값 바로 위인 1은 저장된다")
    void mpJustAboveMinimum() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", 30, Cleric.MIN_MP + 1);

        // then
        assertNotNull(cleric);
        assertEquals(1, cleric.mp);
    }

    @Test
    @DisplayName("MP 최대값 바로 아래 값은 저장된다")
    void mpJustBelowMaximum() {
        // given
        final int mp = Cleric.MAX_MP - 1;

        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", 30, mp);

        // then
        assertNotNull(cleric);
        assertEquals(mp, cleric.mp);
    }

    @Test
    @DisplayName("MP 최대값은 저장된다")
    void mpMaximumBoundary() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", 30, Cleric.MAX_MP);

        // then
        assertNotNull(cleric);
        assertEquals(Cleric.MAX_MP, cleric.mp);
    }

    @Test
    @DisplayName("MP 최대값보다 큰 값은 허용되지 않는다")
    void mpOverMaximumShouldNotBeAccepted() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", 30, Cleric.MAX_MP + 1);

        // then
        assertNull(cleric);
    }

    // --------------------------------------------------
    // 복합 잘못된 값 테스트
    // --------------------------------------------------

    @Test
    @DisplayName("이름이 잘못되면 HP가 정상이어도 생성되지 않는다")
    void invalidNameWithValidHpShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("", 30);

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("이름과 HP가 정상이어도 MP가 범위를 벗어나면 생성되지 않는다")
    void invalidMpShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", 30, Cleric.MAX_MP + 1);

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("이름과 MP가 정상이어도 HP가 범위를 벗어나면 생성되지 않는다")
    void invalidHpShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MAX_HP + 1, 5);

        // then
        assertNull(cleric);
    }
}