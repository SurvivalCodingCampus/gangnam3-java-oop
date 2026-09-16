package com.survivalcoding;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Hero {

    public String name;
    public int hp;
    public void attack() {}
    public void run() {
        System.out.println(this.name + "는 도망쳤다!");
        System.out.println("GAME OVER");
        System.out.println("최종 HP는" +this.hp + "입니다");
    }
    public void sit(int sec) {
        this.hp += sec; //앉은 초 수만큼 HP 가 증가
        System.out.println(this.name + "는 " + sec + "초 앉았다");
        System.out.println("HP가" + sec + "포인트 회복되었다");
    }
    public void slip() {
        this.hp -= 5;
        System.out.println(this.name + "는 넘어졌다!");
        System.out.println("5의 데미지!");
    }
    public void sleep() {
        this.hp = 100;
        System.out.println(this.name + "는 잠을 회복했다!");
    }



}