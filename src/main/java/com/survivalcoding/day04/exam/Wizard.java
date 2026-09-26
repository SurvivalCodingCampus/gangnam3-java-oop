package com.survivalcoding.day04.exam;

public class Wizard {
    static final int HEAL_HP_AMOUNT = 20;
    static final int COST_FOR_HEAL = 10;
    protected static final String HEAL_SKILL_NAME = "힐";
    private static final int MAX_MP = 100;
    private static final int MAX_HP = 100;
    private static final String DEFAULT_NAME = "마법사";
    private static final int MIN_NAME_LENGTH = 3;

    private int hp;
    private int mp;
    private String name;
    private Wand wand;

    // Builder를 통해서만 객체 생성 가능
    protected Wizard(final Builder builder) {
        setHp(builder.hp);
        setMp(builder.mp);
        setName(builder.name);
        setWand(builder.wand);
    }

    // region Func

    /**
     * 스킬 명 출력 : skillName을/를 시전했습니다 대상 HP: curHp
     * heal함수를 통해 대마법사 힐까지 사용
     */
    protected void heal(final Hero hero, final int cost, final int amount, final String skillName) {
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

        System.out.println(skillName + "을 시전했습니다 대상 HP: " + hero.getHp());
    }

    public void heal(final Hero hero) {
        heal(hero, COST_FOR_HEAL, HEAL_HP_AMOUNT, HEAL_SKILL_NAME);
    }

    // endregion Func

    // region Getter Setter

    public int getHp() {
        return hp;
    }

    public int getMp() {
        return mp;
    }

    public String getName() {
        return name;
    }

    public Wand getWand() {
        return wand;
    }

    private void setHp(final int hp) {
        if (hp < 0) {
            this.hp = 0;
            System.out.println("음수여서 0으로 설정함");
            return;
        }
        this.hp = hp;
    }

    private void setMp(final int mp) {
        if (mp < 0) {
            throw new IllegalArgumentException("0이상의 mp 입력");
        }
        this.mp = mp;
    }

    private void setName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름은 null 금지");
        }
        if (name.length() < MIN_NAME_LENGTH) {
            throw new IllegalArgumentException("이름은 3문자 이상");
        }
        this.name = name;
    }

    private void setWand(final Wand wand) {
        if (wand == null) {
            throw new IllegalArgumentException("null 금지");
        }
        this.wand = wand;
    }

    // endregion

    public static class Builder {
        protected int hp = MAX_HP;
        protected int mp = MAX_MP;
        protected String name = DEFAULT_NAME;
        protected Wand wand = new Wand.Builder().build();

        public Builder hp(int hp) {
            this.hp = hp;
            return this;
        }

        public Builder mp(int mp) {
            this.mp = mp;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder wand(Wand wand) {
            this.wand = wand;
            return this;
        }

        public Wizard build() {
            return new Wizard(this);
        }
    }
}