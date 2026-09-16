package com.survivalcoding.day04_calss_instance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClericTest {
    
    @Test
    @DisplayName("selfAid : MP 5를 소비해 최대 HP까지 회복")
    void selfAidTest() {
        // given 준비
        Cleric cleric = new Cleric();
        int mpCost = cleric.SELFAID_MP_COST;
        
        // 1) mp - mpCost < 0 인 경우 1
        // given 준비
        cleric.hp = 35;
        cleric.mp = mpCost - 1;
        
        // when 실행
        cleric.selfAid();
        
        // then 검증
        assertEquals(cleric.mp, mpCost - 1);
        assertEquals(cleric.hp, 35);
        
        
        // 2) mp - mpCost < 0 인 경우 2
        // given 준비
        cleric.hp = 35;
        cleric.mp = mpCost - 10;
        
        // when 실행
        cleric.selfAid();
        
        // then 검증
        assertEquals(cleric.mp, mpCost - 10);
        assertEquals(cleric.hp, 35);
        
        
        // 3) mp - mpCost = 0 인 경우
        // given 준비
        cleric.hp = 35;
        cleric.mp = mpCost;
        
        // when 실행
        cleric.selfAid();
        
        // then 검증
        assertEquals(cleric.mp, 0);
        assertEquals(cleric.hp, cleric.MAX_HP);
        
        
        // 4) mp - mpCost > 0 인 경우
        // given 준비
        cleric.hp = 35;
        cleric.mp = mpCost + 3;
        
        // when 실행
        cleric.selfAid();
        
        // then 검증
        assertEquals(cleric.mp, 3);
        assertEquals(cleric.hp, cleric.MAX_HP);
    }
}