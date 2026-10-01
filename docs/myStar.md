```mermaid
classDiagram
    
    class 테란 {
        <<Interface>>
    }
    
    %% 모든 유닛과 건물이 생물
    %% 피해를 입어도 회복
    class 저그 {
        <<Interface>>
    }
    
    %% 서서히 벙어막 회복
    class 프로토스 {
        <<Interface>>
    }
    
    %% 테란 유닛 생물
    
    class 해병
    
    %% 공격불가 생물유닛 치료, 일꾼 치료
    class 간호사
    
    %% 테란 유닛 기계
    class 탱크
    class 벌처
    
    %% 광맥채취, 기계유닛 수리
    class 일꾼
    
    %% 저그 유닛 생물
    class 저글링
    class 히드라
    
    %% 생물이지만 치료대상X
    class 저그건물
    
    %% 프로토스 생물 유닛 모든 메딕의 치료대상
    class 외계인
    
    %% 기계
    class 드라군
    
    %% 한번에 공격
    %% 한번에 이동
    class 그룹화
    
    %% 속성으로
    class 유닛 {
        <<Interface>>
    }
    
    class 건물 {
        <<Interface>>
    }
    
    class 생물 {
        <<Interface>>
    }
    
    class 기계 {
        <<Interface>>
    }
    
    class 광맥 {
        <<Interface>>
    }
    
    class 힐받기가능 {
        <<Interface>>
        void takeHeal(amount)
    }

    class 힐가능 {
        <<Interface>>
        void heal(힐받기가능)
    }
    
    class 수리받기가능 {
        <<Interface>>
        void takeRepair(amount)
    }
    
    class 수리가능 {
        <<Interface>>
        void repair(수리받기가능)
    }

    class 데미지받기가능 {
        <<Interface>>
        void takeDamage(amount)
    }

    class 어택가능 {
        <<Interface>>
        void attack(데미지받기가능)
    }
    
    class 움직이기가능 {
        <<Interface>>
        void move(direction)
    }
    
    class 방어막가능 {
        <<Interface>>
        void setBarrier(amount)
    }
    
    class 유닛생산 {
        <<Interface>>
        유닛 build()
    }
    
    
    class 광맥채취가능 {
        <<Interface>>
        void Mine(광맥)
    }
    
    %% 기능
    class 서서히뭘한다 {
        <<Interface>>
        void DoSomthing()
    }
```