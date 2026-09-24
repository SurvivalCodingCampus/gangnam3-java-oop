package com.survivalcoding.day04.exam;

public class Wizard {
    protected static final String HEAL_SKILL_NAME = "힐";
    private static final int MAX_MP = 100;
    private static final int HEAL_HP_AMOUNT = 20;
    private static final int COST_FOR_HEAL = 10;
    private static final int MIN_NAME_LENGTH = 3;

    private int hp;
    private int mp;
    private String name;
    private Wand wand;

    public Wizard() {
        setMp(MAX_MP);
    }

    public void heal(final Hero hero) {
        heal(hero, COST_FOR_HEAL, HEAL_HP_AMOUNT, HEAL_SKILL_NAME);
    }

    /**
     * 스킬 명 출력 : skillName을/를 시전했습니다 대상 HP: curHp"
     *
     * @param hero 대상
     * @param cost 마나소모량
     * @param amount - 힐량
     * @param skillName - 스킬명
     */
    public void heal(
            final Hero hero,
            final int cost,
            final int amount,
            final String skillName
    ) {
        if (mp - cost < 0) {
            System.out.println("마나가 부족합니다");
            return;
        }

        int heroHp = hero.getHp();

        if (Hero.MAX_HP <= heroHp) {
            System.out.println("대상 체력이 MAX HP입니다");
            return;
        }

        mp -= cost;
        hero.takeHeal(amount);

        System.out.println(skillName + "을/를 시전했습니다 대상 HP: " + hero.getHp());
    }

    public int getHp() {
        return hp;
    }

    public void setHp(final int hp) {
        if (hp < 0) {
            this.hp = 0;
            System.out.println("음수여서 0으로 설정함");
            return;
        }

        this.hp = hp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(final int mp) {
        if (mp < 0) {
            throw new IllegalArgumentException("0이상의 mp 입력");
        }

        this.mp = mp;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름은 null 금지");
        }

        if (name.length() < MIN_NAME_LENGTH) {
            throw new IllegalArgumentException("이름은 3문자 이상");
        }

        this.name = name;
    }

    public Wand getWand() {
        return wand;
    }

    public void setWand(final Wand wand) {
        if (wand == null) {
            throw new IllegalArgumentException("null 금지");
        }

        this.wand = wand;
    }
}
