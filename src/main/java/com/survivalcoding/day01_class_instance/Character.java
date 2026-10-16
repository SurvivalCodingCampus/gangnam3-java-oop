package com.survivalcoding.day01_class_instance;

// 기능 미정
// 상속의 재료 전용, new 금지
public abstract class Character {
    private String name;
    private int hp;

    public abstract void attack(Slime slime);

    public static void main(String[] args) {

        Character character = new Character() {
            @Override
            public void attack(Slime slime) {
                System.out.println(slime);
            }
        };
    }
}

class MyCharacter extends Character {

    @Override
    public void attack(Slime slime) {

    }
}