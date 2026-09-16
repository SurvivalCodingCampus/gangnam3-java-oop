package com.survivalcoding.day01_class_instance; // 이 클래스가 속한 패키지를 선언한다.

import java.util.Random; // 무작위 랜덤 생성을 위해서 자바의 기본 제공 클래스 가져오기

// 주어진 과제에서 무조건 Cleric은 최대 hp, mp가 지정되어 있으므로 이건 불변해야하는 값이니까 은닉화해봄.
public class Cleric { // 성직자를 추상화하여 표현하는 Cleric 클래스 선언
    private static final int MAX_HP = 50; //성직자 최대 체력 상수 (외부 접근 불가, 은닉화 - private)
    private static final int MAX_MP = 10; //성직자 최대 마나 상수 (외부 접근 불가, 은닉화 - private)

    // 인스턴스 속성 ( field, member parameter) 선언 - 외부에서 직접 수정하지 못하도록 private 은닉화 적용
    private String name; // 불변해야하는 이름
    private int hp; // 불변해야하는 체력
    private int mp; // 불변해야하는 마나

    // 메인 생성자: 이름, 체력, 마나를 모두 전달받아 초기화 하는 생성자 ( 값 안전하게 보정하도록)
    public Cleric(String name, int hp, int mp) {
        this.name = name; // 전달 받은 이름을 객체의 이름 필드에 저장
        this.hp = Math.max(0, Math.min(hp, MAX_HP)); // Math.max, Math.min을 사용해 HP가 0에서 50 사이의 값만 갖도록 안전하게 보정
        this.mp = Math.max(0, Math.min(mp, MAX_MP)); // MP 역시 0에서 10 사이의 값만 갖도록 안전하게 보정
    }

    // 이름, HP만 임의로 받고, MP는 최대값인 10 설정하는 생성자
    public Cleric(String name, int hp) {
        this(name, hp, MAX_MP);
    }

    // 이름을 임의로 받고, HP, MP는 최대값인 생성자
    public Cleric(String name) {
        this(name, MAX_HP, MAX_MP);
    }

    // 기본 생성자: 매개변수 없이 호출될 경우 기본이름 ("김경환) 과 최대 HP, MP설정하는 생성자
    public Cleric() {
        this("김경환", MAX_HP, MAX_MP);
    }

    // Getter 메서드들 : private로 은닉된 필드 값을 외부에서 안전하게 읽어갈 수 있으려면 get을 써야함.
    public String getName() { return name; } //객체 이름 반환
    public int getHp() { return hp; } // 객체 현재 hp 반환
    public int getMp() { return mp; } // 객체 현재 mp 반환
    public int getMaxHp() { return MAX_HP; } // 객체 현재 최대 hp 반환
    public int getMaxMp() { return MAX_MP; } // 객체 현재 최대 mp 반환

    // 데미지를 입는 메서드 - 슬라임 과제에서 영감받음.
    public void takeDamage(int damage) {
        // 현재 체력에서 데미지를 입으면 0 이하로 안깍이게 math.max로 보정.
        this.hp = Math.max(0, this.hp - damage);
    }

    //생존 여부 확인 - HP가 0보다 크면 살아있는 것(true)로 판정한다.
    public boolean isAlive() {
        return this.hp > 0;
    }

    // 셀프 에이드 메서드: 자가치유 -> 마나 5 소비해서 자신의 체력을 최대치로 회복
    public boolean selfAid() {
        if (this.mp < 5) { // MP가 5미만이면 자가치유 기술 못씀
            return false; // 실패 했으니까 false 리턴후 메서드 종료
        }
        this.mp -= 5; // 마나 5 소비
        this.hp = MAX_HP; //체력 최대치인 50 채움
        return true; // 자가치유 성공했으면 true 반환
    }

    // 기도 메서드: 기도하기 -> 기도한 시간(초단위)로 인수 받아 무작위 보정을 더해 마나 회복
    public int pray(int sec) {
        if (sec < 0) return 0; //사용자가 실수로 음수 초를 입력했을 때 회복량을 0으로 해서 보정

        Random random = new Random(); // 무작위 난수 생성을 위한 Random객체 생성
        int healAmount = sec + random.nextInt(3); //기도한 시간(초 단위) + 0~2 사이의 무작위 보정값 ( 0, 1, 2 중 하나 가져옴)

        int actualHeal = Math.min(MAX_MP - this.mp, healAmount);
        // 실제 회복된 양이 최대 MP를 초과할수 없어서 채울 수 있는 MP와 계산된 회복량 중 더 작은값 선택
        this.mp += actualHeal; // 현재 마나에 실제로 회복된 양을 더함.

        return actualHeal; // 실제 회복된 MP양 반환.
    }
}