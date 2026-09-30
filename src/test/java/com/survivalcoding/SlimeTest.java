package com.survivalcoding;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link Slime} 클래스의 테스트.
 * <p>
 * 검증 대상: 생성자 2종(이름 지정 / 기본), {@code takeDamage} 의 HP 감소와 0 하한 보정,
 * 그리고 {@code attack} 가 용사의 HP를 10씩 줄인다는 점.
 * <p>
 * 각 테스트는 Given(준비) → When(실행) → Then(검증) 순서로 주석을 달아
 * 어떤 상황에서 무엇을 확인하는지 드러낸다.
 */
class SlimeTest {

    /**
     * 이름과 HP를 지정해 용사를 만들어 주는 헬퍼.
     * <p>
     * {@code Hero#setName} 은 3글자 미만이면 예외를 던지므로 "준석이"를 쓴다.
     * 여러 테스트가 같은 준비 과정을 반복하므로 한 곳으로 모았다.
     *
     * @param hp 용사에게 줄 HP
     * @return 이름이 설정된 용사
     */
    private Hero heroWithHp(int hp) {
        Hero hero = new Hero();
        hero.setName("준석이");
        hero.setHp(hp);
        return hero;
    }

    @Test
    @DisplayName("슬라임이 피해를 입으면 남은 HP만큼 체력에 감소한다")
    void takeDamage_reducesHp() {
        // Given: hp가 50인 슬라임 준비
        Slime slime = new Slime();
        slime.hp = 50;

        //when: 10의 데미지를 받음
        slime.takeDamage(10);

        //Then: hp가 정상적으로 40이 되는지 확인(초록불)
        assertEquals(40, slime.hp);
    }

    @Test
    @DisplayName("피해량이 HP보다 크면 HP는 0 이 된다")
    void takeDamage_doesNotGoBelowZero() {
        // Given
        Slime slime = new Slime();
        slime.hp = 10;

        // when
        slime.takeDamage(50);

        // then
        assertEquals(0, slime.hp);
    }

    @Test
    @DisplayName("슬라임은 생성시 이름과 최대 HP를 가진다")
    void 슬라임_생성_테스트() {

        // Given
        String name = "슬라임A";

        // When
        Slime slime = new Slime(name);

        // Then
        assertEquals("슬라임A", slime.getName());
        assertEquals(100, slime.getHp());
    }

    @Test
    @DisplayName("슬라임의 HP를 변경할 수 있다")
    void 슬라임_HP_변경_테스트() {

        // Given
        Slime slime = new Slime("슬라임A");

        // When
        slime.setHp(50);

        // Then
        assertEquals(50, slime.getHp());
    }

    @Test
    @DisplayName("슬라임이 영웅을 공격하면 HP가 10 감소한다")
    void 슬라임이_영웅을_공격하면_HP가_10_감소한다() {

        // Given
        Hero hero = heroWithHp(100);
        Slime slime = new Slime("슬라임A");

        // When
        slime.attack(hero);

        // Then
        assertEquals(90, hero.getHp());
    }

    @Test
    @DisplayName("슬라임이 여러번 공격하면 HP가 공격횟수만큼 감소한다")
    void 슬라임이_여러번_공격하면_HP가_공격횟수만큼_감소한다() {

        // Given
        Hero hero = heroWithHp(100);
        Slime slime = new Slime("슬라임A");

        // When
        slime.attack(hero);
        slime.attack(hero);
        slime.attack(hero);

        // Then
        assertEquals(70, hero.getHp());
    }

    @Test
    @DisplayName("영웅의 HP가 0이하로 내려갈수 있다")
    void 영웅의_HP가_0이하로_내려갈수_있다() {

        // Given
        Hero hero = heroWithHp(20);
        Slime slime = new Slime("슬라임A");

        // When
        slime.attack(hero);
        slime.attack(hero);
        slime.attack(hero);

        // Then
        assertEquals(0, hero.getHp());
    }
}
