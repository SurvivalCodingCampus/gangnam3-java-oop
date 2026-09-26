```mermaid
classDiagram

    class Slime {
        - isDead : boolean
        + attack(hero : Hero) void
        + takeDamage(damage : int) void
        - attack(hero : Hero, damage : int)
        - die() void
    }

    class PoisonSlime {
        - POISON_RATE : int [final] $
        - MAX_POISON_COUNT : int [final] $
        - poisonCount : int
        + attack(hero : Hero) void
    }

    class Wizard {
        # HEAL_SKILL_NAME : String [final] $
        + heal(hero : Hero) void
        # heal(hero : Hero, cost : int, amount : int, skillName : String) void
    }
    
    class GreatWizard {
        ~ SUPER_HEAL_SKILL_NAME : String [final] $
        ~ COST_FOR_HEAL : int [final] $
        ~ COST_FOR_SUPER_HEAL : int [final] $
        ~ HEAL_HP_AMOUNT : int [final] $
        - MAX_MP : int [final] $
        + heal(hero : Hero) void
        + superHeal(hero : Hero) void
    }
    
    class Hero {
        - isDead : boolean
        + takeDamage(amount : int) void
        + takeHeal(amount : int) void
        + attack(slime : Slime) void
        - attack(slime : Slime, damage : int) void
    }

    class SuperHero {
        ~ BONUS_DAMAGE : int [final] $
        - isFlying : boolean
        + attack(slime : Slime) void
        + run() void
        + land() void
    }

    Hero <|-- SuperHero
    Wizard <|-- GreatWizard
    Slime <|-- PoisonSlime
    Hero ..> Slime : 공격
    SuperHero ..> Slime : 공격
    Slime ..> Hero : 공격
    PoisonSlime ..> Hero : 공격 / 독 포자
    Wizard ..> Hero : 힐
    GreatWizard ..> Hero : 힐 / 슈퍼 힐

```


