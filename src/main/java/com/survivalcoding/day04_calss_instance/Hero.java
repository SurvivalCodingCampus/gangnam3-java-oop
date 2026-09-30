package com.survivalcoding.day04_calss_instance;

import java.util.Random;

public class Hero {
    // 필드(field), 멤버변수(member variable), 속성(property), 전역변수,
    static final int COUNTER_ATTACK_DAMAGE = 10;
    static final int ATTACK_DAMAGE = 10;
    static final int SLIP_DAMAGE = 5;
    static final int MAX_HP = 100;
    
    static int money = 100;
    
    private Sword sword;
    private String name;
    private int hp;
    
    public Hero() {
        this("김영웅", 100);
    }
    
    public Hero(String name) {
        this(name, 100);
    }
    
    public Hero(String name, int hp) {
        setName(name);
        this.hp = hp;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        Validator.validateNotNull(name);
        Validator.validateMinLength(name, 1);
        Validator.validateMaxLength(name, 8);
        
        this.name = name;
    }
    
    public int getHp() {
        return hp;
    }
    
    public void setHp(int hp) {
//        this.hp = Math.min(MAX_HP, hp);
        Validator.validateAtMost(hp, MAX_HP);
        
        this.hp = hp;
    }
    
    public Sword getSword() { return sword; }
    
    public void setSword(Sword sword) {
        this.sword = sword;
    }
    
    public static void setRandomMoney() {
        money = new Random().nextInt(1000);
    }
    
    public void attack(Slime slime) {
        System.out.printf("%s는 공격했다!%n", this.name);
        setHp(hp - ATTACK_DAMAGE);
    }
    
    public void counterattacked() {
        System.out.println("반격을 받았다");
        setHp(hp - COUNTER_ATTACK_DAMAGE);
        if (hp < 1) {
            die();
        }
    }
    
    public void run() {
        System.out.printf("%s는 도망쳤다!%n", name);
        System.out.println("GAME OVER!");
        System.out.printf("최종 HP는 %d 입니다%n", hp);
    }
    
    public void sit(int sec) {
        setHp(hp + sec);
        
        System.out.printf("%s는 %d초 앉았다%n", name, sec);
        System.out.printf("HP가 %d 포인트 회복되었다%n", sec);
    }
    
    public void slip() {
        setHp(hp - SLIP_DAMAGE);
        
        System.out.printf("%s는 넘어졌다!%n", name);
        System.out.printf("%s의 데미지!%n", SLIP_DAMAGE);
    }
    
    public void sleep() {
        setHp(MAX_HP);
        
        System.out.printf("%s는 잠을 자고 회복했다!%n", this.name);
    }
    
    public void bye() {
        System.out.println("빠이");
    }
    
    private void die() {
        System.out.printf("%s가 죽었다%n", this.name);
    }
}
