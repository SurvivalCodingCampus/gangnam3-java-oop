package com.survivalcoding.day12_abstract_interface;

/**
 * {@link Character} 를 상속한 몬스터.
 * <p>
 * 추상 클래스의 첫 번째 practical 쓰임새를 보여준다.
 * {@code Character} 는 "캐릭터는 공격할 수 있다"까지만 선언하고,
 * <b>어떻게 공격하는지는 이 클래스에서 구체적으로 정한다.</b>
 * <p>
 * 몬스터마다 공격 방식이 다르더라도, 개발자는 빈 메서드를 잊지 않아도 되고
 * 실수로 {@code new Character(...)} 를 만들 일도 없다. 컴파일러가 둘 다 막아준다.
 *
 * <h2>다계층 상속</h2>
 * 이 클래스를 다시 상속하면 계층이 여러 단계가 된다(슬라이드 "다계층의 추상 상속 구조").
 * {@link AdvancedMonster} 가 그 예시다. {@code Character} → {@code Monster} → {@code AdvancedMonster}.
 * <p>
 * 계층이 깊어질수록 자식 쪽에서 {@code super} 로 올라갈 수 있는 대상이 부모로 제한된다.
 * 조부모의 필드나 메서드에 직접 접근하려면 다시 중개해야 한다.
 *
 * @see Character
 * @see AdvancedMonster
 */
public class Monster extends Character {

    /** 몬스터의 현재 HP. */
    private int hp;

    /** 몬스터가 한 번 때릴 때 주는 데미지. */
    private final int attackDamage;

    /**
     * 이름과 HP를 받는 생성자.
     *
     * @param name          몬스터의 이름. {@code super(name) } 으로 부모에게 넘긴다.
     * @param hp            시작 HP
     * @param attackDamage  한 번 공격할 때의 데미지
     */
    public Monster(String name, int hp, int attackDamage) {
        // 부모(Character)의 생성자를 호출해 name 필드를 초기화한다.
        // 부모가 private 필드(name)를 갖고 있으므로 super 를 통해서만 채울 수 있다.
        super(name);

        this.hp = hp;
        this.attackDamage = attackDamage;
    }

    /**
     * 현재 HP를 돌려준다.
     *
     * @return 몬스터의 현재 HP
     */
    public int getHp() {
        return hp;
    }

    /**
     * HP를 줄인다. 0 아래로 내려가지 않는다.
     *
     * @param damage 줄일 대미지
     */
    public void takeDamage(int damage) {
        if (hp - damage < 0) {
            hp = 0;
        } else {
            hp = hp - damage;
        }
    }

    /**
     * 공격한다 —— 부모의 추상 메서드를 구현한 것.
     * <p>
     * {@code @Override} 는 생략해도 컴파일이 되지만, 붙이는 편이 안전하다.
     * 부모 시그니처를 오타로 적으면(예: {@code attak}) {@code @Override} 에서
     * 컴파일 에러가 나므로, 실수를 즉시 잡을 수 있다.
     * <p>
     * 이름은 {@code getName()} 으로 가져온다. 부모의 {@code name} 이 {@code private} 이므로
     * {@code this.name} 처럼 직접 접근할 수 없다.
     */
    @Override
    public void attack() {
        System.out.println(getName() + " HP: " + hp + " 체력으로 " + getName() + "를 공격했습니다.");
    }

    /**
     * 데미지를 돌려준다.
     *
     * @return 이 몬스터가 한 번 때릴 때 주는 데미지
     */
    public int getAttackDamage() {
        return attackDamage;
    }
}