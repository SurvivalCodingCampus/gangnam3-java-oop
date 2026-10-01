package com.survivalcoding;

/**
 * 모험 게임의 진행 과정을 직접 실행해 보는 실행용 클래스.
 * <p>
 * 게임이 아니라, "게임을 조종하는 사람"의 역할이다.
 * 객체 생성 → 값 설정 → 행동 지시 순서로 게임이 진행되는 과정을 보여 준다.
 */
public class Main {

    /**
     * 프로그램의 시작점.
     * <p>
     * 컴파일러가 가장 먼저 찾는 시작 지점이며,
     * {@code public static void main(String[] args)} 라는 고정된 시그니처여야 한다.
     *
     * @param args 실행할 때 전달되는 명령줄 인자. 이 예제에서는 사용하지 않는다.
     */
    public static void main(String[] args) {

        // 가상 세계에 용사를 생성
        Hero hero = new Hero();

        // 생성된 용사에게 최초의 HP와 이름을 설정
        // 이름은 Hero#setName 의 검증(3글자 이상)을 통과해야 하므로 3글자로 지정한다.
        hero.setName("준석이");
        hero.setHp(100);

        System.out.println(
                "용사님의 이름은 " + hero.getName()
                        + "이고, hp는 " + hero.getHp() + "입니다"
        );

        // 가상 세계에 버섯을 생성
        Kinoko kinoko = new Kinoko();
        kinoko.suffix = "A";

        // 슬라임 A 생성
        Slime slime1 = new Slime();
        slime1.hp = 50;
        slime1.suffix = "A";

        // 슬라임 B 생성
        Slime slime2 = new Slime();
        slime2.hp = 48;
        slime2.suffix = "B";

        // 용사에게 '5초 앉기', '넘어지기', '25초 앉기', '도망'을 지시
        hero.sit(5);
        hero.slip();
        hero.sit(25);
        hero.run();

        // 모험의 시작
        hero.slip();
    }
}
