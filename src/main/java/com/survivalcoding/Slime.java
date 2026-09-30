package com.survivalcoding;

public class  Slime {
    public int hp;
    public String suffix;
    final int level = 10;
    private String name;
    private Hero hero;

    public Slime(String name) {
    }

    void run() {
        System.out.println("슬라임 " + suffix + "가 도망갔다");
    }
    void takeDamage(int x){};
       // Getter
    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    // Setter
    public void setHp(int hp) {
        this.hp = hp;
    }

    // 보통 공격
    public void attack(Hero hero) {
        this.hero = hero;
        this.hero = hero;
        System.out.println(name + "이 공격했다!");
        hero.setHp(hero.getHp() - 10);
        System.out.println("10포인트 데미지");
    }
}
