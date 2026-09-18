package com.survivalcoding.day04_calss_instance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClericTest {
    
    // ==========================================
    // 1. 정적 변수 및 생성자 테스트
    // ==========================================
    
    @Test
    @DisplayName("인스턴스를 2개 이상 생성한 경우, 각 인스턴스가 MAX_HP, MAX_MP 값을 공유하는지 확인")
    void testStaticConstantsShared() {
        Cleric cleric1 = new Cleric("Arthur");
        Cleric cleric2 = new Cleric("Uther");
        
        assertEquals(Cleric.MAX_HP, cleric1.MAX_HP);
        assertEquals(Cleric.MAX_MP, cleric1.MAX_MP);
        assertEquals(cleric1.MAX_HP, cleric2.MAX_HP);
        assertEquals(cleric1.MAX_MP, cleric2.MAX_MP);
    }
    
    @Test
    @DisplayName("생성자 : 파라미터 오버로딩에 따라 기본값(MAX)이 올바르게 세팅되는지 확인")
    void testConstructorOverloading() {
        Cleric nameOnly = new Cleric("Arthur");
        Cleric nameAndHp = new Cleric("Uther", 35);
        
        // 이름만 입력한 경우
        assertEquals("Arthur", nameOnly.name);
        assertEquals(Cleric.MAX_HP, nameOnly.hp);
        assertEquals(Cleric.MAX_MP, nameOnly.mp);
        
        // 이름과 HP만 입력한 경우
        assertEquals("Uther", nameAndHp.name);
        assertEquals(35, nameAndHp.hp);
        assertEquals(Cleric.MAX_MP, nameAndHp.mp);
    }
    
    
    // ==========================================
    // 2. selfAid 통합 테스트 (기존 코드 + 경계값)
    // ==========================================
    
    @Test
    @DisplayName("selfAid : MP 상태에 따른 마법 발동 및 회복 검증")
    void selfAidTest() {
        Cleric cleric = new Cleric("Arthur");
        final int MP_COST = Cleric.SELF_AID_MP_COST;
        
        /* 기존 1, 2: MP가 부족한 경우 (경계값 4 포함) */
        cleric.hp = 35;
        cleric.mp = MP_COST - 1; // 4
        cleric.selfAid();
        assertEquals(MP_COST - 1, cleric.mp);
        assertEquals(35, cleric.hp);
        
        cleric.mp = -10;
        cleric.selfAid();
        assertEquals(-10, cleric.mp);
        assertEquals(35, cleric.hp);
        
        /* 기존 3: MP가 정확히 5인 경우 (경계값) */
        cleric.hp = 35;
        cleric.mp = MP_COST; // 5
        cleric.selfAid();
        assertEquals(0, cleric.mp);
        assertEquals(Cleric.MAX_HP, cleric.hp);
        
        /* 기존 4: MP가 넉넉한 경우 */
        cleric.hp = 35;
        cleric.mp = MP_COST + 3; // 8
        cleric.selfAid();
        assertEquals(3, cleric.mp);
        assertEquals(Cleric.MAX_HP, cleric.hp);
        
        /* 추가: 이미 HP가 MAX일 때 사용 (MP만 소모) */
        cleric.hp = Cleric.MAX_HP;
        cleric.mp = Cleric.MAX_MP;
        cleric.selfAid();
        assertEquals(Cleric.MAX_HP, cleric.hp);
        assertEquals(Cleric.MAX_MP - MP_COST, cleric.mp);
    }
    
    
    // ==========================================
    // 3. pray 통합 테스트 (기존 코드 + 경계값)
    // ==========================================
    
    @Test
    @DisplayName("pray : 입력 시간 및 현재 MP 상태에 따른 회복량 검증")
    void prayTest() {
        Cleric cleric = new Cleric("Arthur");
        
        /* 기존 1: 입력 시간이 0 이하인 경우 (경계값 0, 음수) */
        cleric.mp = 5;
        assertEquals(0, cleric.pray(0));
        assertEquals(5, cleric.mp);
        
        assertEquals(0, cleric.pray(-5));
        assertEquals(5, cleric.mp);
        
        /* 기존 2: 시간이 아무리 커도 MAX_MP를 초과할 수 없음 */
        cleric.mp = 5;
        int recoveryAmount = cleric.pray(Cleric.MAX_MP + 10);
        assertEquals(Cleric.MAX_MP, cleric.mp);
        assertTrue(recoveryAmount <= Cleric.MAX_MP);
        
        /* 추가: MP가 이미 MAX일 때 기도하는 경우 (경계값) */
        cleric.mp = Cleric.MAX_MP;
        assertEquals(0, cleric.pray(3));
        assertEquals(Cleric.MAX_MP, cleric.mp);
        
        /* 추가: MP가 1 모자랄 때 한계치 방어 (경계값) */
        cleric.mp = Cleric.MAX_MP - 1; // 9
        assertEquals(1, cleric.pray(5)); // 시간은 5초지만 1만 회복되어야 함
        assertEquals(Cleric.MAX_MP, cleric.mp);
        
        /* 추가: 정상 범위 기도 시 랜덤 보정값 로직 검증 */
        cleric.mp = 0;
        int sec = 2;
        int randomRecovery = cleric.pray(sec);
        
        // 회복량 반환값과 실제 mp 증가량이 일치하는지 확인
        assertEquals(randomRecovery, cleric.mp);
        // 회복량이 sec(2) ~ sec+2(4) 범위 내인지 확인
        assertTrue(randomRecovery >= sec && randomRecovery <= sec + 2);
    }
    
    @Test
    @DisplayName("pray : sec가 int 최대값일 때 오버플로우로 인해 MP가 깎이는 버그 검증")
    void prayOverflowTest() {
        Cleric cleric = new Cleric("Arthur", Cleric.MAX_HP, 5); // 현재 MP: 5
        
        // int 표현 최대값을 입력으로 전달
        int overflowSec = Integer.MAX_VALUE;
        
        int recoveryAmount = cleric.pray(overflowSec);
        
        // 음수가 반환되어 MP가 오버플로우로 인해 감소했는지 확인
        System.out.println("회복량: " + recoveryAmount); // 음수 출력 (-2147483647 등)
        System.out.println("결과 MP: " + cleric.mp);         // MP가 음수로 떨어짐
        
        // 정상이라면 최소 0 이상이어야 함
        assertTrue(recoveryAmount >= 0, "회복량이 음수가 되어서는 안 됩니다.");
    }
}