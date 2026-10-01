```mermaid
classDiagram
    
    class Battle
    
    class 1vs1Battle
    class 2vs2Battle
    
    class Creature

    class Terran
    
    class Zerg
    class Protoss
    
    class Unit
    class Structure
    
    class Human
    class Alien
    class Bionic
    class BionicUnit
    class Mechanic
    class Mechanic
    

    Battle <|-- 1vs1Battle 
    Battle  <|-- 2vs2Battle 
    
    Creature <|-- Terran
    Creature <|-- Zerg
    Creature <|-- Protoss
    
    Terran <|-- Bionic 
    Terran <|-- Mechanic 
    
    Bionic <|-- 

    Structure <|-- Bionic
    Structure <|-- Mechanic
    
    Unit <|-- Bionic
    Unit <|-- Mechanic
```