package com.survivalcoding;

/**
 * 연습문제 3 - 독 슬라임(PoisonSlime)
 * <p>
 * "독 슬라임은 슬라임 중에서도 특히 독 공격이 되는 것"이므로
 * {@link Slime} 을 상속받아 일반 공격에 독 공격을 덧붙인다.
 * <p>
 * 올바른 상속인지는 "is-a 원칙"으로 판단한다.
 * PoisonSlime is a Slime → 독 슬라임은 슬라임의 한 종류이므로 상속해도 된다.
 * <p>
 * 이 클래스는 인스턴스가 "일반 슬라임에 무엇이 추가되었는지"를 보여주는 예시다.
 * 필드는 하나(poisonCount)뿐이고, 메서드는 하나(attack)만 재정의했다.
 * 나머지 이름, hp, takeDamage, run 은 모두 Slime 에서 그대로 물려받는다.
 * 이것이 상속의 핵심이다. 복사 붙여넣기였다면 이 부분들을 다시 적어야 했을 것이다.
 */
public class PoisonSlime extends Slime {

    /**
     * 독 공격이 가능한 횟수.
     * <p>
     * 연습문제의 요구사항 "아무나 수정 금지"를 지키기 위해
     * {@code private} 으로 선언하고, getter 도 setter 도 만들지 않는다.
     * 같은 클래스 안에서만 값을 읽고 줄일 수 있다.
     */
    private int poisonCount = 5;

    /**
     * 이름만 지정하는 생성자.
     * <p>
     * {@code super(name)} 로 부모 {@link Slime} 의 생성자를 호출해 이름을 넘긴다.
     * 상속받은 필드이므로 super 로 부모가 채우게 한다.
     * 이 한 줄이 없으면 "부모 클래스의 생성자를 호출하지 않으면 에러"가 발생한다.
     *
     * @param name 독 슬라임의 이름
     */
    public PoisonSlime(String name) {
        super(name);
    }

    /**
     * 독 슬라임의 공격. {@link Slime#attack(Hero)} 를 재정의(override)한다.
     * <p>
     * 연습문제 3의 요구절 순서 그대로 구현한다.
     * <ol>
     *     <li>먼저 보통 슬라임과 같은 공격을 한다 ({@code super.attack})</li>
     *     <li>독 공격 횟수가 남아 있으면 추가로 독 공격을 수행한다</li>
     *     <li>독 공격이 끝나면 횟수를 1 줄인다</li>
     * </ol>
     * <p>
     * 1번에서 super 를 호출하는 것이 중요하다.
     * 부모의 공격 로직을 다시 베끼지 않고 재사용하는 것이다.
     * 부모의 공격이 바뀌면 자식도 자동으로 따라 바뀐다.
     *
     * @param hero 공격 대상 용사
     */
    @Override
    public void attack(Hero hero) {

        // 1. 보통 슬라임과 같은 공격 (부모의 메서드 재사용)
        super.attack(hero);

        // 2. 독 공격 횟수가 남아 있는 경우에만 추가로 공격한다
        if (poisonCount > 0) {

            // 3. 독 포자 살포
            System.out.println("추가로, 독 포자를 살포했다!");

            // 4. 독 데미지 = 용사 HP / 5
            //    int 형끼리 나누면 소수점 이하가 자동으로 버려진다(정수 나눗셈).
            //    예) 62 / 5 = 12.4 -> 12
            //    반드시 HP 를 감소시키기 "전에" 계산해야 한다.
            //    감소시킨 뒤 계산하면 데미지가 항상 0 이 되기 때문이다.
            final int poisonDamage = hero.getHp() / 5;

            // 5. 용사의 HP를 독 데미지만큼 감소시킨다
            hero.setHp(hero.getHp() - poisonDamage);

            // 6. 데미지 표시
            System.out.println(poisonDamage + "포인트 데미지");

            // 7. 독 공격 횟수 1 감소
            poisonCount--;
        }
    }
}
