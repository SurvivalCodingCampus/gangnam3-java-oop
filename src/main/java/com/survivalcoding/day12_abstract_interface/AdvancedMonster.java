package com.survivalcoding.day12_abstract_interface;

/**
 * {@link Monster} 를 다시 상속한 고도화된 몬스터 —— 다계층 상속 구조.
 * <p>
 * 상속 계층을 세 단계로 늘린 예시다.
 * <pre>
 * Character (추상)
 *    └─ Monster
 *         └─ AdvancedMonster   ← 여기
 * </pre>
 * <p>
 * <b>부모의 {@code attack()} 을 그대로 쓰지 않고 다시 정의했다.</b>
 * 상위 클래스의 메서드는 "기본 구현"으로 남겨 두고, 하위 클래스만 다르게 가져가는 방식이다.
 * 덕분에 {@link Monster} 를 그대로 쓰는 다른 코드에는 영향이 없다.
 * <p>
 * <b>다층 상속의 단점</b> — 자식은 조부모의 {@code private} 필드에 직접 접근할 수 없다.
 * {@code Character.name} 은 {@code private} 이므로 이 클래스에서는 {@code getName()} 으로만 얻는다.
 * 계층이 깊어질수록 "값을 위로 올리는 통로(getter)} 를 계속 만들어야" 해서 상속이 무거워진다.
 * 그래서 상속은 깊게 이어지지 않게 하고, 변해야 할 부분은 인터페이스로 떼어내는 편이 낫다.
 *
 * @see Character
 * @see Monster
 */
public class AdvancedMonster extends Monster {

    /** 이 몬스터를 처치했을 때 얻는 보상 경험치. */
    private final int rewardExp;

    /**
     * 상위 클래스의 정보를 그대로 넘겨받는 생성자.
     *
     * @param name          몬스터의 이름
     * @param hp            시작 HP
     * @param attackDamage  한 번 공격할 때의 데미지
     * @param rewardExp     처치 보상 경험치
     */
    public AdvancedMonster(String name, int hp, int attackDamage, int rewardExp) {
        // Monster 의 생성자를 호출한다. Monster 가 super(name) 을 호출하므로
        // Character 까지 연쇄적으로 초기화된다. (생성자 호출은 항상 1개만, 가장 위쪽 super 를 호출)
        super(name, hp, attackDamage);

        this.rewardExp = rewardExp;
    }

    /**
     * 보상 경험치를 돌려준다.
     *
     * @return 처치 시 얻는 경험치
     */
    public int getRewardExp() {
        return rewardExp;
    }

    /**
     * 공격한다 —— {@link Monster#attack()} 를 다시 정의한 것.
     * <p>
     * 부모는 "체력을 알려주며 공격한다"였지만, 이 클래스는 대미지 숫자를 직접 찍는다.
     * {@code super.attack()} 로 부모 동작을 재사용할 수도 있었지만, 출력 형태가 달라
     * 여기서는 새로 정의했다.
     */
    @Override
    public void attack() {
        System.out.println("[고도화] " + getName() + "이(가) " + getAttackDamage() + "의 힘으로 공격합니다.");
    }

    /**
     * 죽었을 때의 처리.
     * <p>
     * {@link Monster#takeDamage(int)} 로 HP 를 0까지 깎은 뒤 보상을 준다.
     * 다계층 상속의 장점 — 부모에게 배운 메서드에 자식만의 동작을 덧붙일 수 있다.
     */
    public void die() {
        takeDamage(getHp());
        System.out.println(getName() + "을(를) 처치했습니다. 경험치 " + rewardExp + " 획득!");
    }
}