package com.survivalcoding.day01_class_instance;

import java.util.Random;

class Cleric {
    static final int MP_COST = 5;
    static final int maxHp = 50;
    static final int maxMp = 10;

    String name;
    int hp;
    int mp;

    Cleric(String name, int hp, int mp) {
        this.name = name;
        this.hp = hp;
        this.mp = mp;
    }

    Cleric(String name, int hp) {
        this(name, hp, maxMp);
    }

    Cleric(String name) {
        this(name, maxHp, maxMp);
    }

    // 테스트 검증을 위한 main 메서드
    public static void main(String[] args) {
        System.out.println("=== Cleric 기능 테스트 시작 ===\n");

        // [시나리오 1] pray를 3초 하면 회복량이 3~5 사이여야 함
        System.out.println("[테스트 1] pray(3) 회복량 검증 (3~5 범위)");
        Cleric testCleric1 = new Cleric("테스터1", 10, 0); // MP가 0인 상태에서 시작
        int recovery = testCleric1.pray(3);

        if (recovery >= 3 && recovery <= 5) {
            System.out.printf("-> 성공: 실제 회복량은 %d 입니다. (MP: %d/%d)\n\n", recovery, testCleric1.mp, maxMp);
        } else {
            System.out.printf("-> 실패: 범위 외의 회복량입니다. (%d)\n\n", recovery);
        }

        // [시나리오 2] mp가 5일 때 selfAid를 하면 hp가 maxHp(50)가 되어야 함
        System.out.println("[테스트 2] selfAid() 사용 시 HP 만땅 및 MP 소모 검증");
        Cleric testCleric2 = new Cleric("테스터2", 10, 5); // HP 10, MP 5인 상태
        testCleric2.selfAid();

        if (testCleric2.hp == maxHp && testCleric2.mp == 0) {
            System.out.printf("-> 성공: HP가 최대치(%d)로 회복되었고 MP가 %d이 되었습니다.\n\n", testCleric2.hp, testCleric2.mp);
        } else {
            System.out.printf("-> 실패: HP 변동(%d) 또는 MP 변동(%d)이 올바르지 않습니다.\n\n", testCleric2.hp, testCleric2.mp);
        }

        System.out.println("=== 테스트 종료 ===");
    }

    void selfAid() {
        if (mp < 5) {
            return;
        }
        mp -= 5;
        hp = maxHp;
    }

    int pray(int sec) {
        int bonus = new Random().nextInt(3);
        int recovery = sec + bonus;

        int maxRecovery = this.maxMp - this.mp;
        int actualRecovery = Math.min(recovery, maxRecovery);

        this.mp += actualRecovery;
        return actualRecovery;
    }
}
