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

class ClericTestDay05 {

    // ==================================================
    // CreateOrNull(String name)
    // ==================================================

    @Test
    @DisplayName("이름만 입력하면 HP와 MP는 최대값으로 초기화된다")
    void createWithNameShouldSetMaxHpAndMp() {
        // given
        final String name = "홍길동";

        // when
        final Cleric cleric =
                Cleric.CreateOrNull(name);

        // then
        assertNotNull(cleric);
        assertEquals(name, cleric.name);
        assertEquals(Cleric.MAX_HP, cleric.hp);
        assertEquals(Cleric.MAX_MP, cleric.mp);
    }

    @Test
    @DisplayName("이름이 null이면 생성되지 않는다")
    void nullNameShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull(null);

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("이름이 빈 문자열이면 생성되지 않는다")
    void emptyNameShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("");

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("이름이 공백 한 칸이면 생성되지 않는다")
    void singleBlankNameShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull(" ");

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("이름이 여러 공백이면 생성되지 않는다")
    void multipleBlankNameShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("     ");

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("일반적인 이름은 정상적으로 저장된다")
    void validNameShouldBeStored() {
        // given
        final String name = "홍길동";

        // when
        final Cleric cleric =
                Cleric.CreateOrNull(name);

        // then
        assertNotNull(cleric);
        assertEquals(name, cleric.name);
    }


    // ==================================================
    // CreateOrNull(String name, int hp)
    // ==================================================

    @Test
    @DisplayName("이름과 HP만 입력하면 MP는 최대값으로 초기화된다")
    void createWithNameAndHpShouldSetMaxMp() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", 30);

        // then
        assertNotNull(cleric);
        assertEquals("홍길동", cleric.name);
        assertEquals(30, cleric.hp);
        assertEquals(Cleric.MAX_MP, cleric.mp);
    }

    @Test
    @DisplayName("HP 최소값은 정상적으로 저장된다")
    void minimumHpShouldBeAccepted() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MIN_HP);

        // then
        assertNotNull(cleric);
        assertEquals(Cleric.MIN_HP, cleric.hp);
    }

    @Test
    @DisplayName("HP 최소값 바로 위의 값은 정상적으로 저장된다")
    void hpJustAboveMinimumShouldBeAccepted() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MIN_HP + 1);

        // then
        assertNotNull(cleric);
        assertEquals(Cleric.MIN_HP + 1, cleric.hp);
    }

    @Test
    @DisplayName("HP 최대값 바로 아래의 값은 정상적으로 저장된다")
    void hpJustBelowMaximumShouldBeAccepted() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MAX_HP - 1);

        // then
        assertNotNull(cleric);
        assertEquals(Cleric.MAX_HP - 1, cleric.hp);
    }

    @Test
    @DisplayName("HP 최대값은 정상적으로 저장된다")
    void maximumHpShouldBeAccepted() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MAX_HP);

        // then
        assertNotNull(cleric);
        assertEquals(Cleric.MAX_HP, cleric.hp);
    }

    @Test
    @DisplayName("HP 최소값보다 작으면 생성되지 않는다")
    void hpBelowMinimumShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MIN_HP - 1);

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("HP 최대값보다 크면 생성되지 않는다")
    void hpAboveMaximumShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MAX_HP + 1);

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("이름이 잘못되고 HP가 정상이면 생성되지 않는다")
    void invalidNameAndValidHpShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("", 30);

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("이름이 정상이고 HP가 잘못되면 생성되지 않는다")
    void validNameAndInvalidHpShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MAX_HP + 1);

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("이름과 HP가 모두 잘못되면 생성되지 않는다")
    void invalidNameAndInvalidHpShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("", Cleric.MAX_HP + 1);

        // then
        assertNull(cleric);
    }


    // ==================================================
    // CreateOrNull(String name, int hp, int mp)
    // ==================================================

    @Test
    @DisplayName("정상적인 이름, HP, MP를 입력하면 Cleric이 생성된다")
    void validNameHpAndMpShouldCreateCleric() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", 30, 5);

        // then
        assertNotNull(cleric);
        assertEquals("홍길동", cleric.name);
        assertEquals(30, cleric.hp);
        assertEquals(5, cleric.mp);
    }


    // ==================================================
    // HP 경계값
    // ==================================================

    @Test
    @DisplayName("HP 최소값과 정상 MP를 입력하면 생성된다")
    void minimumHpWithValidMpShouldBeAccepted() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MIN_HP, 5);

        // then
        assertNotNull(cleric);
        assertEquals(Cleric.MIN_HP, cleric.hp);
    }

    @Test
    @DisplayName("HP 최대값과 정상 MP를 입력하면 생성된다")
    void maximumHpWithValidMpShouldBeAccepted() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MAX_HP, 5);

        // then
        assertNotNull(cleric);
        assertEquals(Cleric.MAX_HP, cleric.hp);
    }

    @Test
    @DisplayName("HP 최소값보다 작으면 생성되지 않는다")
    void hpBelowMinimumWithMpShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MIN_HP - 1, 5);

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("HP 최대값보다 크면 생성되지 않는다")
    void hpAboveMaximumWithMpShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MAX_HP + 1, 5);

        // then
        assertNull(cleric);
    }


    // ==================================================
    // MP 경계값
    // ==================================================

    @Test
    @DisplayName("MP 최소값은 정상적으로 저장된다")
    void minimumMpShouldBeAccepted() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", 30, Cleric.MIN_MP);

        // then
        assertNotNull(cleric);
        assertEquals(Cleric.MIN_MP, cleric.mp);
    }

    @Test
    @DisplayName("MP 최소값 바로 위의 값은 정상적으로 저장된다")
    void mpJustAboveMinimumShouldBeAccepted() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", 30, Cleric.MIN_MP + 1);

        // then
        assertNotNull(cleric);
        assertEquals(Cleric.MIN_MP + 1, cleric.mp);
    }

    @Test
    @DisplayName("MP 최대값 바로 아래의 값은 정상적으로 저장된다")
    void mpJustBelowMaximumShouldBeAccepted() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", 30, Cleric.MAX_MP - 1);

        // then
        assertNotNull(cleric);
        assertEquals(Cleric.MAX_MP - 1, cleric.mp);
    }

    @Test
    @DisplayName("MP 최대값은 정상적으로 저장된다")
    void maximumMpShouldBeAccepted() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", 30, Cleric.MAX_MP);

        // then
        assertNotNull(cleric);
        assertEquals(Cleric.MAX_MP, cleric.mp);
    }

    @Test
    @DisplayName("MP 최소값보다 작으면 생성되지 않는다")
    void mpBelowMinimumShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", 30, Cleric.MIN_MP - 1);

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("MP 최대값보다 크면 생성되지 않는다")
    void mpAboveMaximumShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", 30, Cleric.MAX_MP + 1);

        // then
        assertNull(cleric);
    }


    // ==================================================
    // name / hp / mp 정상·비정상 조합
    // ==================================================

    @Test
    @DisplayName("이름만 잘못되면 생성되지 않는다")
    void invalidNameOnlyShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("", 30, 5);

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("HP만 잘못되면 생성되지 않는다")
    void invalidHpOnlyShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MAX_HP + 1, 5);

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("MP만 잘못되면 생성되지 않는다")
    void invalidMpOnlyShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", 30, Cleric.MAX_MP + 1);

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("이름과 HP가 잘못되면 생성되지 않는다")
    void invalidNameAndHpShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("", Cleric.MAX_HP + 1, 5);

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("이름과 MP가 잘못되면 생성되지 않는다")
    void invalidNameAndMpShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("", 30, Cleric.MAX_MP + 1);

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("HP와 MP가 잘못되면 생성되지 않는다")
    void invalidHpAndMpShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MAX_HP + 1, Cleric.MAX_MP + 1);

        // then
        assertNull(cleric);
    }

    @Test
    @DisplayName("이름, HP, MP가 모두 잘못되면 생성되지 않는다")
    void allInvalidValuesShouldReturnNull() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("", Cleric.MAX_HP + 1, Cleric.MAX_MP + 1);

        // then
        assertNull(cleric);
    }


    // ==================================================
    // 최소 / 최대 경계값 조합
    // ==================================================

    @Test
    @DisplayName("HP와 MP가 모두 최소값이면 생성된다")
    void minimumHpAndMinimumMpShouldBeAccepted() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MIN_HP, Cleric.MIN_MP);

        // then
        assertNotNull(cleric);
        assertEquals(Cleric.MIN_HP, cleric.hp);
        assertEquals(Cleric.MIN_MP, cleric.mp);
    }

    @Test
    @DisplayName("HP와 MP가 모두 최대값이면 생성된다")
    void maximumHpAndMaximumMpShouldBeAccepted() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MAX_HP, Cleric.MAX_MP);

        // then
        assertNotNull(cleric);
        assertEquals(Cleric.MAX_HP, cleric.hp);
        assertEquals(Cleric.MAX_MP, cleric.mp);
    }

    @Test
    @DisplayName("HP 최소값과 MP 최대값이면 생성된다")
    void minimumHpAndMaximumMpShouldBeAccepted() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MIN_HP, Cleric.MAX_MP);

        // then
        assertNotNull(cleric);
        assertEquals(Cleric.MIN_HP, cleric.hp);
        assertEquals(Cleric.MAX_MP, cleric.mp);
    }

    @Test
    @DisplayName("HP 최대값과 MP 최소값이면 생성된다")
    void maximumHpAndMinimumMpShouldBeAccepted() {
        // when
        final Cleric cleric =
                Cleric.CreateOrNull("홍길동", Cleric.MAX_HP, Cleric.MIN_MP);

        // then
        assertNotNull(cleric);
        assertEquals(Cleric.MAX_HP, cleric.hp);
        assertEquals(Cleric.MIN_MP, cleric.mp);
    }
}