package com.survivalcoding;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlimeTest {
    @Test
    @DisplayName("슬라임이 피혜를 입으면 남은 HP만큼 체력에 감소한다")
    void takeDamage_reducesHp(){
        // Given: hp가 50인 슬라임 준비
        Slime slime = new Slime();
        slime.hp = 50;

        //when: 10의 데미지를 받음
        slime.takeDamage(10);

        //Then: hp가 정상적으로 40이 되는지 확인(초록불)
        assertEquals(40, slime.hp);
    }

}