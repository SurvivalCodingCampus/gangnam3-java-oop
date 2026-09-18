package com.survivalcoding;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cleric 테스트 (JUnit 5)
 *
 * Cleric.java 와 같은 패키지(com.survivalcoding)에 둬야 필드에 직접 접근된다.
 * (필드가 default 접근 제어자라서 같은 패키지에서만 보임)
 *
 * 구성
 *   1. 통과해야 하는 테스트  : 생성자 3종, selfAid, pray 정상 동작
 *   2. 일부러 실패하는 테스트: 현재 코드의 버그를 드러내는 용도 (메서드명 앞에 fail_ )
 */
class ClericTest {

    // ===================== 1. 통과하는 테스트 =====================

    @Test
    @DisplayName("생성자(이름, HP, MP) - 세 값이 그대로 들어간다")
    void constructor_full() {
        Cleric c = new Cleric("힐러", 50, 30);

        assertEquals("힐러", c.Name);
        assertEquals(50, c.HP);
        assertEquals(30, c.MP);
    }

    @Test
    @DisplayName("생성자(이름, HP) - MP는 Max_MP(67)로 채워진다")
    void constructor_nameAndHp() {
        Cleric c = new Cleric("힐러", 50);

        assertEquals(50, c.HP);
        assertEquals(67, c.MP);   // Max_MP
    }

    @Test
    @DisplayName("생성자(이름) - HP/MP 모두 최대치로 채워진다")
    void constructor_nameOnly() {
        Cleric c = new Cleric("힐러");

        assertEquals(100, c.HP);  // Max_HP
        assertEquals(67, c.MP);   // Max_MP
    }

    @Test
    @DisplayName("selfAid - MP가 충분하면 MP 5 소모 + HP 최대 회복")
    void selfAid_enoughMp() {
        Cleric c = new Cleric("힐러", 10, 6);

        c.selfAid();

        assertEquals(100, c.HP);
        assertEquals(1, c.MP);
    }

    @Test
    @DisplayName("selfAid 경계값 - MP가 정확히 5여도 발동한다")
    void selfAid_exactly5() {
        Cleric c = new Cleric("힐러", 10, 5);

        c.selfAid();

        assertEquals(100, c.HP);
        assertEquals(0, c.MP);
    }

    @Test
    @DisplayName("selfAid 경계값 - MP가 4면 아무것도 바뀌지 않는다")
    void selfAid_notEnoughMp() {
        Cleric c = new Cleric("힐러", 10, 4);

        c.selfAid();

        assertEquals(10, c.HP);   // HP 그대로
        assertEquals(4, c.MP);    // MP 그대로
    }

    @RepeatedTest(20)
    @DisplayName("pray 정상 - 반환값과 실제 MP 증가량이 같고, 보너스는 0 또는 1")
    void pray_normal() {
        Cleric c = new Cleric("힐러", 100, 6);
        int before = c.MP;

        int recovered = c.pray(10);

        assertEquals(before + recovered, c.MP);
        assertTrue(recovered == 10 || recovered == 11,
                "REUSE 보너스는 0 또는 1이어야 하는데 반환값=" + recovered);
    }

    @RepeatedTest(20)
    @DisplayName("pray 상한 - MP는 Max_MP(67)를 절대 넘지 않는다")
    void pray_capped() {
        Cleric c = new Cleric("힐러", 100, 60);   // 60 + 10 or 11 -> 67 로 잘려야 함

        c.pray(10);

        assertEquals(67, c.MP);
    }

    @Test
    @DisplayName("pray 음수 - MP는 변하지 않는다")
    void pray_negative_doesNotChangeMp() {
        Cleric c = new Cleric("힐러", 100, 6);

        c.pray(-1);

        assertEquals(6, c.MP);
    }

    // ============== 2. 현재 코드에서 실패하는 테스트 (버그 노출) ==============

    @Test
    @DisplayName("[실패] pray 상한 - 반환값이 실제 회복량과 같아야 한다")
    void fail_pray_capped_returnsZero() {
        Cleric c = new Cleric("힐러", 100, 60);
        int before = c.MP;

        int recovered = c.pray(10);

        // 기대: 7  /  실제: 0
        // 원인: MP=Max_MP; 를 먼저 실행한 뒤 return Max_MP-MP; 라서 항상 0
        // 수정: int r = Max_MP - MP; MP = Max_MP; return r;
        assertEquals(c.MP - before, recovered);
    }

    @Test
    @DisplayName("[실패] pray 음수 - 회복량 0을 반환해야 한다")
    void fail_pray_negative_returns67() {
        Cleric c = new Cleric("힐러", 100, 6);

        int recovered = c.pray(-1);

        // 기대: 0  /  실제: 67
        // 67은 "Max_MP만큼 회복됨"과 구분이 안 되는 값이라 호출한 쪽이 판단할 수 없다.
        // 수정: 음수 검사를 맨 위로 올리고 return 0; (또는 예외 던지기)
        assertEquals(0, recovered);
    }

    @RepeatedTest(20)
    @DisplayName("[실패/불안정] MP가 가득 찬 상태의 pray(-1) - 난수에 따라 결과가 달라진다")
    void fail_pray_negative_whenMpFull() {
        Cleric c = new Cleric("힐러", 100, 67);

        int recovered = c.pray(-1);

        // REUSE=1 -> add_MP=0  -> 첫 분기(상한)  -> 0 반환
        // REUSE=0 -> add_MP=-1 -> 음수 분기      -> 67 반환
        // 같은 입력인데 실행할 때마다 결과가 바뀐다.
        assertEquals(0, recovered);
    }

    @Test
    @DisplayName("[실패] 생성자 - Max_HP/Max_MP를 넘는 값을 막아야 한다")
    void fail_constructor_noRangeCheck() {
        Cleric c = new Cleric("힐러", 9999, 9999);

        assertTrue(c.HP <= c.Max_HP, "HP가 최대치를 넘었다: " + c.HP);
        assertTrue(c.MP <= c.Max_MP, "MP가 최대치를 넘었다: " + c.MP);
    }
}