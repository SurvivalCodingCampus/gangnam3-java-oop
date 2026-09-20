```mermaid
classDiagram
    class Slime {
- hp : int
- suffix : String
- level : int [final]
~ run() void
}

class Wizard {
+ MIN_MP : int [final] $
- hp : int
- mp : int
- name : String
- wand : Wand
- heal(Hero) void
+ getHp() int
+ setHp(int)
+ getName() String
+ setName(String)
+ getWand() Wand
+ setWand(Wand)
}

class King {
+ callHero(Hero) void
}

class Sword {
- name : String
- damage : int
    }

class Wand {
- name : String
- power : double
+ getName() String
+ setName(String)
+ getPower() double
+ setPower(double)
}

class Hero {
+ MAX_HP : int [final] $
- money : int $
- name : String
- hp : int
- sword : Sword
+ Hero()
+ Hero(name : String [final])
+ setRandomMoney() void
+ bye() void
- die() void
+ sleep() void
+ attack() void
+ attack(Slime) void
+ run() void
+ slip() void
+ sit(sec : int) void
+ setHp(int) void
+ getHp() int
+ getName() String
+ setName(String) void
    }

class Person {
- name : String [final]
- birthYear : String [final]
- thisYear : int [final]
+ Person(name : String , birthYear : int)
+ getAge() int
}

class Cleric {
+ COST_FOR_SELF_AID : int [final] $
+ MAX_CORRECTION_VALUE : int [final] $
+ MAX_HP : int [final] $
+ MAX_MP : int [final] $
+ MIN_HP : int [final] $
+ MIN_MP : int [final] $
- hp : int
- mp : int
- name : String
+ Cleric(name : String , hp : int , mp : int )
+ Cleric(name : String , hp : int )
+ Cleric(name : String )
+ selfAid() void
+ pray(durationSecond : int) int
+ getMp() int
+ getHp() int
+ getName() String
}

Wizard *-- Wand
Wizard ..> Hero : 힐
King ..> Hero : 부름
Hero *-- Sword
Hero ..> Slime : 공격 
```


