package com;

import com.survivalcoding.Cleric;

/**
 * 성직자(Cleric) 클래스의 동작을 직접 실행해 보는 실행용 클래스.
 * <p>
 * 패키지가 {@code com} 인 이유: 이 파일은 {@code src/main/java/com/} 아래에 있고,
 * Java 는 소스 파일의 위치가 선언한 패키지와 일치해야 한다.
 * <p>
 * (참고) {@code com.survivalcoding} 에도 {@code Main} 이 하나 더 있다.
 * 같은 패키지에서 같은 이름의 클래스를 두 번 선언하면 컴파일 에러가 나므로
 * 이쪽은 패키지를 {@code com} 으로 분리한 것이다.
 */
public class Main {

    /**
     * 프로그램의 시작점.
     *
     * @param args 명령줄 인자. 이 예제에서는 사용하지 않는다.
     */
    static void main(String[] args) {

        // 성직자를 생성한다. 이름을만 주면 HP, MP 는 각각 최대치로 초기화된다.
        Cleric cleric = new Cleric("세라핌");

        // Cleric 의 필드는 public 이므로 직접 읽을 수 있다.
        System.out.println("이름 : " + cleric.name);
        System.out.println("HP : " + cleric.hp);
        System.out.println("MP : " + cleric.mp);

        // HP 를 20 으로 낮춘 뒤, 자기 자신을 회복해 본다.
        cleric.hp = 20;

        cleric.selfAid();

        System.out.println("HP : " + cleric.hp);
        System.out.println("MP : " + cleric.mp);

        // 3초 기도해 MP 회복량을 확인한다.
        int recovery = cleric.pray(3);

        System.out.println("회복된 MP : " + recovery);
        System.out.println("현재 MP : " + cleric.mp);
    }
}
