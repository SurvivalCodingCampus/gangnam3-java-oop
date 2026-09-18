package com.survivalcoding;

public class Hero {

    static int money = 100;

    private String name;
    private String sword;
    private int hp;

    // 이름 getter
    public String getName() {
        return this.name;
    }

    // 이름 setter
    public void setName(String name) {
        if (name == null || name.length() < 3) {
            throw new IllegalArgumentException(
                    "이름은 null일 수 없고 3문자 이상이어야 합니다."
            );
        }

        this.name = name;
    }

    // HP getter
    public int getHp() {
        return this.hp;
    }

    // HP setter
    public void setHp(int hp) {
        if (hp < 0) {
            this.hp = 0;
        } else {
            this.hp = hp;
        }
    }

    // 검 getter
    public String getSword() {
        return this.sword;
    }

    // 검 setter
    public void setSword(String sword) {
        this.sword = sword;
    }

    public void attack() {
    }

    public void run() {
        System.out.println(this.name + "는 도망쳤다!");
        System.out.println("GAME OVER");
        System.out.println("최종 HP는 " + this.hp + "입니다");
    }

    public void sit(int sec) {
        this.hp += sec;

        System.out.println(this.name + "는 " + sec + "초 앉았다");
        System.out.println("HP가 " + sec + "포인트 회복되었다");
    }

    public void slip() {
        this.hp -= 5;

        if (this.hp < 0) {
            this.hp = 0;
        }

        System.out.println(this.name + "는 넘어졌다!");
        System.out.println("5의 데미지!");
    }

    public void sleep() {
        this.hp = 100;
        System.out.println(this.name + "는 잠을 자고 HP를 회복했다!");
    }

    public void bye() {
        System.out.println("용자는 이별을 고했다. 빠이");
    }

    private void die() {
        System.out.println(this.name + "는 죽었다");
        System.out.println("Game Over");
    }

    public void attack(Kinoko enemy) {
        System.out.println("반격을 받았다");
        System.out.println(
                "괴물버섯" + enemy.suffix + "로부터 2포인트의 반격을 받았다"
        );

        this.hp -= 2;

        if (this.hp < 1) {
            die();
        }
    }
}