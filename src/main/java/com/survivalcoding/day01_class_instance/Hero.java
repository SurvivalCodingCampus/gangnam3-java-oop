package com.survivalcoding.day01_class_instance;

/**
 * 1장 - 클래스와 인스턴스 수업에서 다룬 가장 초기 형태의 용사 클래스.
 * <p>
 * 이 클래스는 이후 {@code com.survivalcoding.Hero} 로 발전한다.
 * 비교 예시로 남아 있는 "처음 모습"의 코드이므로,
 * getter/setter 나 검증도 없고 메서드도 대부분 비어 있다.
 * <p>
 * (주의) 이 클래스 이름은 {@code com.survivalcoding.Hero} 와 같지만
 * 다른 패키지에 있으므로 서로 완전히 다른 클래스다.
 * 같은 이름의 클래스를 다른 패키지에 두는 것은 허용된다.
 */
public class Hero {

    // 필드(field), 멤버변수(member variable), 속성(property), 전역변수
    // 모두 같은 것을 부르는 이름들이다. 접근 지정자를 생략하면 기본값으로 package-private 이다.
    // 즉 com.survivalcoding.day01_class_instance 안에서만 접근할 수 있다.
    String name;
    int hp;

    // 기능 (method)
    // 메서드가 요구사항을 아직 정의하지 않은 상태이므로 빈 몸체만 둔다.
    void attack() {
    }

    void run() {
    }

    void sit(int sec) {
    }

    void slip() {
    }

    /**
     * 잠을 자면 HP가 200 이 된다.
     * <p>
     * 연습 중 일부러 100 이 아닌 200 을 쓴 값이다.
     * (day01_class_instance 의 HeroTest 가 200 을 기대하고 있다)
     * 나중 {@code com.survivalcoding.Hero} 는 MAX_HP = 100 을 사용한다.
     */
    void sleep() {
        hp = 200;
    }
}
