package com.survivalcoding;

/**
 * 왕(King)을 나타내는 클래스.
 * <p>
 * 다른 클래스({@link Hero})를 "구성(aggregation)"해서 사용하는 예를 보여 준다.
 * 왕이 용사를 상속하는 것이 아니라, 왕이 매개변수로 용사를 전달받아
 * 사용하는 관계다.
 * <p>
 * 상속(has-a 가 아니라 is-a)과 구성(has-a)은 다르다.
 * - 상속: SuperHero is a Hero
 * - 구성: King has a Hero
 */
public class King {

    /**
     * 용사를 불러들여 인사하고, 이내 작별 인사를 건넨다.
     * <p>     * 용사의 필드에 직접 접근하지 않고 public getter 를 통해 읽는다.
     * 그래야 나중에 {@link Hero} 의 내부 구조가 바뀌어도 왕은 영향을 받지 않는다.
     *
     * @param hero 왕을 찾아온 용사
     */
    void callHero(Hero hero) {
        hero.getName();

        System.out.println("용사님, 저희 왕국에 와주셔서 감사합니다");

        //Getter 메서드를 통해 안전하게 값에 접근
        System.out.println("용사님의 이름은" + hero.getName() + "이고, hp는 " + hero.getHp() + "입니다");
        hero.bye();

    }

    /**
     * 용사와 대화한 뒤 도망가라고 한다.
     *
     * @param hero 대화할 용사
     */
    void talk(Hero hero) {
        System.out.println("왕 : 우리 성에 어서오시오. 용사" + hero.getName() + "이여");

        System.out.println("왕 : 긴 여행에 피로하겠군");
        System.out.println("왕 : 우선 아랫 마을을 보고 와도 좋소." + "그럼 또 봅시다");
        hero.run();
    }
}
