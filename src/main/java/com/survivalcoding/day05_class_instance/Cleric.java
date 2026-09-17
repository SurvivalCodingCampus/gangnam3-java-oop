package com.survivalcoding.day05_class_instance;

import java.util.Random;

// 성직자의 상태, 생성 방법, 행동을 정의한다.
public class Cleric {

    // 모든 Cleric 객체가 공유하는 최대값이다.
    // static: 클래스에 속하는 필드 / final: 초기화 후 재대입 불가
    static final int MAX_HP = 50;
    static final int MAX_MP = 10;

    // 성직자에게 공통으로 적용되는 행동 규칙이다.
    static final int SELFAID_MP_COST = 5;
    static final int PRAY_RECOVERY_RANDOM_BOUND = 3;

    // 현재 이름·HP·MP는 각 객체가 별도로 가진다.
    String name;
    int hp;
    int mp;

    // ① new Cleric("아서스", 40, 5)
    // 가장 많은 정보를 받는 생성자에 필드 초기화를 모은다.
    Cleric(String name, int hp, int mp) {
        this.name = name; // 객체의 name 필드에 전달받은 이름을 저장한다.
        this.hp = hp;
        this.mp = mp;
    }

    // ② new Cleric("아서스", 35)
    // 전달받은 HP는 유지하고, 생략된 MP만 최대값으로 채운다.
    Cleric(String name, int hp) {
        this(name, hp, MAX_MP);
    }

    // ③ new Cleric("아서스")
    // 이름만 전달하면 HP와 MP를 각각 최대값으로 채운다.
    Cleric(String name) {
        this(name, MAX_HP, MAX_MP);
    }

    // 인수 없는 Cleric()은 선언하지 않는다.
    // 생성자를 이미 정의했으므로 컴파일러도 기본 생성자를 자동 추가하지 않는다.

    // 인수도 반환값도 없는 행동이다.
    void selfAid() {
        // MP가 5 이상일 때만 비용을 지불하고 HP를 완전히 회복한다.
        if (this.mp >= SELFAID_MP_COST) {
            this.mp -= SELFAID_MP_COST;
            this.hp = MAX_HP;
        }
    }

    // 인수는 기도 시간, 반환값은 실제로 회복된 MP 양이다.
    int pray(int sec) {
        // 기존 코드의 정책: 0초 또는 음수 시간에는 아무 변화도 없다.
        if (sec <= 0) {
            return 0;
        }

        // nextInt(3)은 0, 1, 2 중 하나를 반환한다. 3초 고정이라는 뜻이 아니다.
        Random random = new Random();
        int randomPoint = random.nextInt(PRAY_RECOVERY_RANDOM_BOUND);

        // 큰 int 시간에 보정값을 더할 때 음수로 넘치지 않도록 먼저 long으로 변환한다.
        long requestedRecovery = (long) sec + randomPoint; // 이게 더 효율적이라고 해서 짜봤습니다.

        // 현재 MP가 0~10이라는 전제에서, 남은 공간만큼만 회복할 수 있다.
        int recoveryAmount =
                (int) Math.min(requestedRecovery, MAX_MP - this.mp);

        this.mp += recoveryAmount;

        // 최종 MP가 아니라 이번 호출로 증가한 MP를 반환한다.
        return recoveryAmount;
    }

    // 실행 예시를 같은 파일에 둔다. 이 메서드를 실행하면 아서스 객체들이 생성된다.
    // main은 생성자가 아니며, new Cleric(...)을 호출할 때 자동 실행되지 않는다.
    public static void main(String[] args) {
        // 클래스명으로 공유 상수에 접근한다. 객체 생성 전에도 사용할 수 있다.
        System.out.println("공유 최대 HP: " + Cleric.MAX_HP);
        System.out.println("공유 최대 MP: " + Cleric.MAX_MP);

        // 세 가지 생성자를 호출하여 서로 다른 아서스 객체를 만든다.
        // 인스턴스 필드로 선언하지 않고, main 안의 지역 변수로 선언한다.
        Cleric cleric1 = new Cleric("아서스", 40, 5); // name, hp, mp 받기
        Cleric cleric2 = new Cleric("아서스", 35); // name, hp 받기
        Cleric cleric3 = new Cleric("아서스"); // name 받기

        System.out.println("cleric1: " + cleric1.name
                + ", HP=" + cleric1.hp + ", MP=" + cleric1.mp);
        System.out.println("cleric2: " + cleric2.name
                + ", HP=" + cleric2.hp + ", MP=" + cleric2.mp);
        System.out.println("cleric3: " + cleric3.name
                + ", HP=" + cleric3.hp + ", MP=" + cleric3.mp);

        // 첫 번째 객체만 마법을 사용한다. HP 50, MP 0이 된다.
        cleric1.selfAid();
        System.out.println("cleric1 selfAid 후: HP="
                + cleric1.hp + ", MP=" + cleric1.mp);

        // MP가 비어 있으므로 3초 기도로 3~5를 회복한다.
        int recoveredMp = cleric1.pray(3);
        System.out.println("cleric1 기도 회복량: " + recoveredMp);
        System.out.println("cleric1 기도 후 MP: " + cleric1.mp);

        // 다른 객체의 상태는 그대로다.
        System.out.println("cleric2 상태 유지: HP="
                + cleric2.hp + ", MP=" + cleric2.mp);

        // 인수 없는 생성자가 없으므로 주석을 해제하면 컴파일 오류다.
        // Cleric unnamed = new Cleric();
    }
}
