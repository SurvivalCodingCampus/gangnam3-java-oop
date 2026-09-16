package com.survivalcoding.day04_calss_instance;

public class Hero {
    // 필드(field), 멤버변수(member variable), 속성(property), 전역변수,
    String name;
    int hp;
    
    // 기능(method)
    void attack() {}
    void run() {}
    void sit(int sec) {}
    void slip() {}
    void sleep() {
        this.hp = 100;  // 100 = magic number. 이게 뭔데? 라고 물어볼 수 있음. max_hp 이런 식으로 따로 설정해줘야 함
        System.out.println(this.name + "는 잠을 자고 회복했다!");
    }
}
