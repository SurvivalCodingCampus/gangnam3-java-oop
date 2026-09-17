package com.survivalcoding.day04_calss_instance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClericTest {
    // given 준비
    Cleric cleric = new Cleric();
    
    @Test
    @DisplayName("selfAid : MP 5를 소비해 최대 HP까지 회복")
    void selfAidTest() {
        // given 준비
        final int MP_COST = cleric.SELFAID_MP_COST;
        
        /* 1 */
        // given 준비 : mp - mpCost < 0 인 경우 1
        int tempMp = MP_COST - 1;
        cleric.hp = 35;
        cleric.mp = tempMp;
        
        // when 실행
        cleric.selfAid();
        
        // then 검증 : mp 유지, hp 유지
        assertEquals(tempMp, cleric.mp);  // 예상값, 내 값
        assertEquals(35, cleric.hp);
        
        
        /* 2 */
        // given 준비 : mp - mpCost < 0 인 경우 2
        tempMp = -10;
        cleric.hp = 35;
        cleric.mp = tempMp;
        
        // when 실행
        cleric.selfAid();
        
        // then 검증 : mp 유지, hp 유지
        assertEquals(tempMp, cleric.mp);
        assertEquals(35, cleric.hp);
        
        
        /* 3 */
        // given 준비 : mp - mpCost = 0 인 경우
        cleric.hp = 35;
        cleric.mp = MP_COST;
        
        // when 실행
        cleric.selfAid();
        
        // then 검증 : mp 0, hp는 max
        assertEquals(0, cleric.mp);
        assertEquals(cleric.MAX_HP, cleric.hp);
        
        
        /* 4 */
        // given 준비 : mp - mpCost > 0 인 경우
        cleric.hp = 35;
        cleric.mp = MP_COST + 3;
        
        // when 실행
        cleric.selfAid();
        
        // then 검증 : MP_COST 뺀 만큼 남고 hp는 max
        assertEquals(3, cleric.mp);
        assertEquals(cleric.MAX_HP, cleric.hp);
    }
    
    
    @Test
    @DisplayName("pray : 입력 시간 + '0~2 랜덤값' 보정, 보정한 값 반환")
    void prayTest() {
        /* 1 */
        // given 준비 : 입력 시간 <= 0 인 경우
        int inputSec = 0;
        int beforeMp = cleric.mp;
        
        // when 실행
        int recoveryAmount = cleric.pray(inputSec);
        
        // then 검증 : 회복량 0, mp 변동 없음
        assertEquals(0, recoveryAmount);
        assertEquals(beforeMp, cleric.mp);
        
        
        /* 2 */
        // given 준비 : 입력 시간 >= MAX_MP
        inputSec = cleric.MAX_MP + 10;
        beforeMp = cleric.mp;
        
        // when 실행
        recoveryAmount = cleric.pray(inputSec);
        
        // then 검증 : 회복량이 아무리 커도 MAX_MP가 max여야 함,
        assertEquals(cleric.MAX_MP, cleric.mp);
        assert (recoveryAmount <= cleric.MAX_MP);
    }
}