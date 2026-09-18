package com.survivalcoding.day04_calss_instance;

import java.util.Random;

public class Hero {
    // 필드(field), 멤버변수(member variable), 속성(property), 전역변수,
    static int money = 100;
    
    Sword sword;
    
    private String name;
    private int hp;
    
    // 생성자
    Hero() {
        this("김영웅", 100);
    }
    
    Hero(String name) {
        this(name, 100);
    }
    
    Hero(String name, int hp) {
        setName(name);
        this.hp = hp;
    }
    
    // getter / setter
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
        this.hp = hp;
    }
    
    // 기능(method)
    static void setRandomMoney() {
        money = new Random().nextInt(1000);
    }
    
    public void attack() {
        System.out.printf("%s는 공격했다!%n", this.name);
        System.out.println("적에게 5 포인트의 데미지를 주었다!");
    }
    
    public void counterattacked() {
        System.out.println("반격을 받았다");
        hp -= 10;
        if (hp < 1) {
            die();
        }
    }
    
    void run() {
        System.out.printf("%s는 도망쳤다!%n", this.name);
        System.out.println("GAME OVER!");
        System.out.printf("최종 HP는 %d 입니다%n", this.hp);
    }
    
    void sit(int sec) {
        this.hp += sec;  // 앉은 시간 만큼 HP 증가
        
        System.out.printf("%s는 %d초 앉았다%n", this.name, sec);
        System.out.printf("HP가 %d 포인트 회복되었다%n", sec);
    }
    
    void slip() {
        this.hp -= 5;
        
        System.out.printf("%s는 넘어졌다!%n", this.name);
        System.out.println("5의 데미지!");
    }
    
    void sleep() {
        this.hp = 100;  // 100 = magic number. 이게 뭔데? 라고 물어볼 수 있음. max_hp 이런 식으로 따로 설정해줘야 함
        System.out.printf("%s는 잠을 자고 회복했다!%n", this.name);
    }
    
    public void bye() {
        System.out.println("빠이");
    }
    
    private void die() {
        System.out.printf("%s가 죽었다%n", this.name);
    }
}
