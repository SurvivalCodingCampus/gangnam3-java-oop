package com.survivalcoding.day01_class_instance; // 이 클래스가 속한 패키지를 선언한다.

import java.util.Random; // 무작위 랜덤 생성을 위해서 자바의 기본 제공 클래스 가져오기

public class ClericMain { // 성직자를 추상화하여 표현하는 Cleric 클래스 선언
    int MAX_HP = 50; // 성직자 최대 체력 상수 (static, private 제거)
    int MAX_MP = 10; // 성직자 최대 마나 상수 (static, private 제거)

    // 인스턴스 속성 선언 (private 제거)
    String name; // 성직자 이름
    int hp; // 성직자 체력
    int mp; // 성직자 마나

    // 메인 생성자: 이름, 체력, 마나를 모두 전달받아 초기화 하는 생성자
    public ClericMain(String name, int hp, int mp) {
        this.name = name; // 전달 받은 이름을 객체의 이름 필드에 저장
        this.hp = Math.max(0, Math.min(hp, MAX_HP)); // Math.max, Math.min을 사용해 HP가 0에서 50 사이의 값만 갖도록 안전하게 보정
        this.mp = Math.max(0, Math.min(mp, MAX_MP)); // MP 역시 0에서 10 사이의 값만 갖도록 안전하게 보정
    }

    // 데미지를 입는 메서드 - 슬라임 과제에서 영감받음.
    public void takeDamage(int damage) {
        // 현재 체력에서 데미지를 입으면 0 이하로 안 깎이게 Math.max로 보정.
        this.hp = Math.max(0, this.hp - damage);
    }

    // 생존 여부 확인 - HP가 0보다 크면 살아있는 것(true)로 판정한다.
    public boolean isAlive() {
        return this.hp > 0;
    }

    // 셀프 에이드 메서드: 자가치유 -> 마나 5 소비해서 자신의 체력을 최대치로 회복
    public boolean selfAid() {
        if (this.mp < 5) { // MP가 5 미만이면 자가치유 기술 못 씀
            return false; // 실패했으니까 false 리턴 후 메서드 종료
        }
        this.mp -= 5; // 마나 5 소비
        this.hp = this.MAX_HP; // 체력 최대치인 50 채움
        return true; // 자가치유 성공했으면 true 반환
    }

    // 기도 메서드: 기도하기 -> 기도한 시간(초 단위)로 인수 받아 무작위 보정을 더해 마나 회복
    public int pray(int sec) {
        if (sec < 0) return 0; // 사용자가 실수로 음수 초를 입력했을 때 회복량을 0으로 해서 보정

        Random random = new Random(); // 무작위 난수 생성을 위한 Random 객체 생성
        int healAmount = sec + random.nextInt(3); // 기도한 시간(초 단위) + 0~2 사이의 무작위 보정값

        int actualHeal = Math.min(this.MAX_MP - this.mp, healAmount);
        // 실제 회복된 양이 최대 MP를 초과할 수 없어서 채울 수 있는 MP와 계산된 회복량 중 더 작은 값 선택
        this.mp += actualHeal; // 현재 마나에 실제로 회복된 양을 더함.

        return actualHeal; // 실제 회복된 MP 양 반환.
    }
}