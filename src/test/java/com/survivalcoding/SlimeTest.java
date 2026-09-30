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

=======
public class SlimeTest {

    @Test
    void 슬라임_생성_테스트() {

        // Given
        String name = "슬라임A";

        // When
        Slime slime = new Slime(name);

        // Then
        assertNotNull(slime);
        assertEquals("슬라임A", slime.getName());
        assertEquals(100, slime.getHp());
    }


    @Test
    void 슬라임_HP_변경_테스트() {

        // Given
        Slime slime = new Slime("슬라임A");

        // When
        slime.setHp(50);

        // Then
        assertEquals(50, slime.getHp());
    }


    @Test
    void 슬라임이_영웅을_공격하면_HP가_10_감소한다() {

        // Given
        Hero hero = new Hero();
        hero.setName("준석이");
        hero.setHp(100);

        Slime slime = new Slime("슬라임A");

        // When
        slime.attack(hero);

        // Then
        assertEquals(90, hero.getHp());
    }


    @Test
    void 슬라임이_여러번_공격하면_HP가_공격횟수만큼_감소한다() {

        // Given
        Hero hero = new Hero();
        hero.setName("준석이");
        hero.setHp(100);

        Slime slime = new Slime("슬라임A");

        // When
        slime.attack(hero);
        slime.attack(hero);
        slime.attack(hero);

        // Then
        assertEquals(70, hero.getHp());
    }


    @Test
    void 영웅의_HP가_0이하로_내려갈수_있다() {

        // Given
        Hero hero = new Hero();
        hero.setName("준석이");
        hero.setHp(20);

        Slime slime = new Slime("슬라임A");

        // When
        slime.attack(hero);
        slime.attack(hero);
        slime.attack(hero);

        // Then
        assertEquals(0, hero.getHp());
    }
>>>>>>> be77039 (feat : 2026.09.23 박강원_ 8장)
}