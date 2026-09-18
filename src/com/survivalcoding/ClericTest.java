package com.survivalcoding;

public class ClericTest {

    public static void main(String[] args) {
        System.out.println("=== Cleric 검증 테스트 시작 ===");

        try {
            testConstructorAllArgs();
            testConstructorOmitMp();
            testConstructorNameOnly();
            testSelfAidSuccess();
            testSelfAidFailDueToLowMp();
            testPrayRecoveryRange();
            testPrayMaxMpLimit();

            System.out.println("\n🎉 [성공] 모든 테스트 케이스를 통과했습니다!");
        } catch (AssertionError e) {
            System.err.println("\n❌ [실패] 테스트 중 오류가 발생했습니다!");
            e.printStackTrace();
        }
    }

    // 1. 생성자 테스트 (모두 지정)
    static void testConstructorAllArgs() {
        Cleric cleric = new Cleric("아서스", 40, 5);
        if (!cleric.name.equals("아서스") || cleric.hp != 40 || cleric.mp != 5) {
            throw new AssertionError("생성자1 실패: 값이 올바르게 초기화되지 않음");
        }
    }

    // 2. 생성자 테스트 (MP 생략)
    static void testConstructorOmitMp() {
        Cleric cleric = new Cleric("아서스2", 35);
        if (cleric.hp != 35 || cleric.mp != Cleric.maxMp) {
            throw new AssertionError("생성자2 실패: MP가 maxMp로 초기화되지 않음");
        }
    }

    // 3. 생성자 테스트 (이름만 지정)
    static void testConstructorNameOnly() {
        Cleric cleric = new Cleric("아서스3");
        if (cleric.hp != Cleric.maxHp || cleric.mp != Cleric.maxMp) {
            throw new AssertionError("생성자3 실패: HP/MP가 기본값으로 초기화되지 않음");
        }
    }

    // 4. selfAid 성공 검증
    static void testSelfAidSuccess() {
        Cleric cleric = new Cleric("치유사", 10, 5);
        cleric.selfAid();
        if (cleric.hp != Cleric.maxHp || cleric.mp != 0) {
            throw new AssertionError("selfAid 성공 케이스 실패: HP가 가득 차지 않거나 MP 소모가 안 됨");
        }
    }

    // 5. selfAid MP 부족 시 실패 검증
    static void testSelfAidFailDueToLowMp() {
        Cleric cleric = new Cleric("치유사", 10, 4);
        cleric.selfAid();
        if (cleric.hp != 10 || cleric.mp != 4) {
            throw new AssertionError("selfAid 마나 부족 케이스 실패: 마나가 없는데 치유가 작동함");
        }
    }

    // 6. pray 랜덤 범위 검증
    static void testPrayRecoveryRange() {
        Cleric cleric = new Cleric("기도자", 50, 0);
        int actualRecovery = cleric.pray(3); // 3초 기도 -> 3 + (0~2) = 3~5 회복되어야 함

        if (actualRecovery < 3 || actualRecovery > 5) {
            throw new AssertionError("pray 범위 실패: 3초 기도 시 회복량이 3~5를 벗어남 -> " + actualRecovery);
        }
        if (cleric.mp != actualRecovery) {
            throw new AssertionError("pray 반영 실패: 회복량과 현재 마나가 일치하지 않음");
        }
    }

    // 7. pray 최대 MP 한계 검증
    static void testPrayMaxMpLimit() {
        Cleric cleric = new Cleric("기도자", 50, 9);
        int actualRecovery = cleric.pray(5); // 5초 기도하지만 남은 공간은 1뿐

        if (actualRecovery != 1 || cleric.mp != Cleric.maxMp) {
            throw new AssertionError("pray 최대치 실패: maxMp를 초과하여 회복되었거나 계산이 틀림");
        }
    }
}