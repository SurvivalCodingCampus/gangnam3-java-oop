```mermaid
classDiagram

    class Asset {
        <<abstract>>
    }
    
    class TangibleAsset {
        <<abstract>>
    }
        
    class Computer {
        
    }
    
    class Book {
        
    }
    
    class IntangibleAsset {
        <<abstract>>
    }
    
    class Patent {
        
    }

    Asset <|-- TangibleAsset
    Asset <|-- IntangibleAsset 
    IntangibleAsset <|-- Patent 
    TangibleAsset <|-- Computer 
    TangibleAsset <|-- Book 
    

```