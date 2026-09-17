package com.survivalcoding.day05_class_instance;

// JUnit 5의 기본 기능만 사용한다. static import와 @Nested는 사용하지 않는다.
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Cleric: 생성자, 인스턴스, 공유 필드와 행동")
class ClericTest {

    // given: 입력값이나 초기 상태를 준비한다.
    // when: 검증 대상인 생성자 또는 메서드를 실행한다.
    // then: 실행 결과가 예상과 같은지 확인한다.
    // 연속 동작을 검증하는 테스트는 when/then을 단계별로 반복한다.

    // 1. 과제의 세 가지 생성 방법

    @Test
    @DisplayName("아서스를 HP 40, MP 5로 생성할 수 있다")
    void createWithNameHpAndMp() {
        // given: 생성자에 전달할 이름, HP, MP를 준비한다.
        String name = "아서스";
        int hp = 40;
        int mp = 5;

        // when: 세 인수를 받는 생성자로 객체를 생성한다.
        Cleric cleric = new Cleric(name, hp, mp);

        // then: 전달한 값이 각 필드에 저장되었는지 검증한다.
        Assertions.assertEquals("아서스", cleric.name);
        Assertions.assertEquals(40, cleric.hp);
        Assertions.assertEquals(5, cleric.mp);
    }

    @Test
    @DisplayName("아서스와 HP 35만 지정하면 MP는 최대값 10이다")
    void createWithNameAndHp() {
        // given: 이름과 HP를 준비하고 MP는 지정하지 않는다.
        String name = "아서스";
        int hp = 35;

        // when: 두 인수를 받는 생성자로 객체를 생성한다.
        Cleric cleric = new Cleric(name, hp);

        // then: 이름과 HP는 전달한 값이고, 생략한 MP는 10인지 검증한다.
        Assertions.assertEquals("아서스", cleric.name);
        Assertions.assertEquals(35, cleric.hp);
        Assertions.assertEquals(10, cleric.mp);
    }

    @Test
    @DisplayName("아서스라는 이름만 지정하면 HP 50, MP 10이다")
    void createWithName() {
        // given: 이름만 준비한다.
        String name = "아서스";

        // when: 이름만 받는 생성자로 객체를 생성한다.
        Cleric cleric = new Cleric(name);

        // then: 이름이 저장되고 HP와 MP가 각각 최댓값인지 검증한다.
        Assertions.assertEquals("아서스", cleric.name);
        Assertions.assertEquals(50, cleric.hp);
        Assertions.assertEquals(10, cleric.mp);
    }

    @Test
    @DisplayName("생성자에 명시한 HP 0, MP 0을 기본값으로 덮어쓰지 않는다")
    void preserveExplicitZeroValues() {
        // given: HP와 MP를 모두 0으로 지정한다.
        // 0은 직접 지정한 값이다. 값을 생략한 경우와 구분해야 한다.
        String name = "아서스";
        int hp = 0;
        int mp = 0;

        // when: 준비한 값으로 객체를 생성한다.
        Cleric cleric = new Cleric(name, hp, mp);

        // then: 명시한 0이 기본값으로 덮어써지지 않았는지 검증한다.
        Assertions.assertEquals(0, cleric.hp);
        Assertions.assertEquals(0, cleric.mp);
    }

    @Test
    @DisplayName("생성자에 명시한 최대 HP 50, 최대 MP 10을 저장한다")
    void preserveExplicitMaximumValues() {
        // given: 정상 범위의 최대 HP와 최대 MP를 준비한다.
        String name = "아서스";
        int hp = 50;
        int mp = 10;

        // when: 준비한 최댓값으로 객체를 생성한다.
        Cleric cleric = new Cleric(name, hp, mp);

        // then: 지정한 HP와 MP가 그대로 저장되었는지 검증한다.
        Assertions.assertEquals(50, cleric.hp);
        Assertions.assertEquals(10, cleric.mp);
    }

    // new Cleric()은 컴파일 단계에서 금지된다.
    // 실행할 수 없는 코드를 테스트 본문에 넣지 않고 별도 컴파일 검사로 확인했다.

    // 2. 강의의 공유 필드와 참조 개념

    @Test
    @DisplayName("객체 생성 없이 클래스명으로 최대 HP와 MP에 접근한다")
    void accessSharedMaximumValues() {
        // given: 과제에서 정한 최대값을 준비한다. Cleric 객체는 생성하지 않는다.
        int expectedMaxHp = 50;
        int expectedMaxMp = 10;

        // when: 클래스명으로 공유 필드의 값을 읽는다.
        // static이 없으면 이 접근 자체가 컴파일되지 않는다.
        int actualMaxHp = Cleric.MAX_HP;
        int actualMaxMp = Cleric.MAX_MP;

        // then: 공유 필드의 값이 과제의 최대값과 같은지 검증한다.
        Assertions.assertEquals(expectedMaxHp, actualMaxHp);
        Assertions.assertEquals(expectedMaxMp, actualMaxMp);
        // final 재대입 금지는 코드 검토와 별도 컴파일 검사로 확인한다.
    }

    @Test
    @DisplayName("new를 두 번 사용하면 상태가 독립적인 객체 두 개가 생긴다")
    void differentInstancesHaveIndependentState() {
        // given: 첫 번째 인스턴스인 아서스를 준비한다.
        Cleric arthas = new Cleric("아서스", 40, 5);

        // when 1: 별도의 new로 두 번째 인스턴스를 생성한다.
        Cleric jaina = new Cleric("제이나", 30, 8);

        // then 1: 서로 다른 객체이며 첫 번째 객체의 상태가 유지되는지 검증한다.
        Assertions.assertNotSame(arthas, jaina);
        Assertions.assertEquals("아서스", arthas.name);
        Assertions.assertEquals(40, arthas.hp);
        Assertions.assertEquals(5, arthas.mp);

        // when 2: 첫 번째 인스턴스만 selfAid를 사용한다.
        arthas.selfAid();

        // then 2: 첫 번째 객체만 변하고 두 번째 객체는 그대로인지 검증한다.
        Assertions.assertEquals(50, arthas.hp);
        Assertions.assertEquals(0, arthas.mp);
        Assertions.assertEquals("제이나", jaina.name);
        Assertions.assertEquals(30, jaina.hp);
        Assertions.assertEquals(8, jaina.mp);
    }

    @Test
    @DisplayName("참조 변수를 대입하면 같은 인스턴스를 가리킨다")
    void assignedReferencePointsToSameInstance() {
        // given: 참조를 복사할 원본 객체를 준비한다.
        // 강의 4쪽의 참조 대입을 Cleric으로 확인하는 보충 시나리오다.
        Cleric first = new Cleric("아서스", 40, 5);

        // when: 참조를 대입한 뒤 두 번째 변수를 통해 행동을 실행한다.
        Cleric second = first;
        second.selfAid();

        // then: 같은 객체를 가리키며 첫 번째 변수에서도 변경이 보이는지 검증한다.
        Assertions.assertSame(first, second);
        Assertions.assertEquals(50, first.hp);
        Assertions.assertEquals(0, first.mp);
    }

    // 3. 처음 제시한 두 가지 기본 테스트 시나리오

    @Test
    @DisplayName("MP가 0인 아서스가 3초 기도하면 3~5를 회복한다")
    void prayForThreeSeconds() {
        // given: 3~5를 모두 회복할 수 있도록 MP가 비어 있는 객체를 준비한다.
        // 이름만 지정하면 MP가 10이므로, 이 테스트에서는 MP 0을 직접 지정한다.
        Cleric cleric = new Cleric("아서스", 40, 0);

        // when: 3초 기도한다.
        int recoveredMp = cleric.pray(3);

        // then: 회복량 범위와 MP 증가를 확인하고 HP·이름이 유지되는지 검증한다.
        Assertions.assertTrue(recoveredMp >= 3 && recoveredMp <= 5);
        Assertions.assertEquals(recoveredMp, cleric.mp);
        Assertions.assertEquals(40, cleric.hp);
        Assertions.assertEquals("아서스", cleric.name);
    }

    @Test
    @DisplayName("HP 40, MP 5에서 selfAid를 사용하면 HP 50, MP 0이다")
    void selfAidWithFiveMp() {
        // given: 회복이 필요한 HP와 정확히 비용만큼의 MP를 준비한다.
        Cleric cleric = new Cleric("아서스", 40, 5);

        // when: selfAid를 사용한다.
        cleric.selfAid();

        // then: HP가 50으로 회복되고 MP 5가 소비되며 이름은 유지되는지 검증한다.
        Assertions.assertEquals(50, cleric.hp);
        Assertions.assertEquals(0, cleric.mp);
        Assertions.assertEquals("아서스", cleric.name);
    }

    // 4. selfAid 경계값 및 반복 사용

    @Test
    @DisplayName("MP가 비용보다 1 부족한 4이면 HP와 MP가 그대로다")
    void selfAidWithFourMp() {
        // given: 사용 비용 5보다 1 부족한 MP 4를 준비한다.
        Cleric cleric = new Cleric("아서스", 40, 4);

        // when: MP가 부족한 상태에서 selfAid 사용을 시도한다.
        cleric.selfAid();

        // then: HP와 MP가 모두 그대로인지 검증한다.
        Assertions.assertEquals(40, cleric.hp);
        Assertions.assertEquals(4, cleric.mp);
    }

    @Test
    @DisplayName("MP가 비용보다 1 많은 6이면 HP 50, MP 1이다")
    void selfAidWithSixMp() {
        // given: 사용 비용 5보다 1 많은 MP 6을 준비한다.
        Cleric cleric = new Cleric("아서스", 40, 6);

        // when: selfAid를 사용한다.
        cleric.selfAid();

        // then: HP는 50이고 비용을 차감한 MP는 1인지 검증한다.
        Assertions.assertEquals(50, cleric.hp);
        Assertions.assertEquals(1, cleric.mp);
    }

    @Test
    @DisplayName("MP가 0이면 selfAid를 사용해도 상태가 그대로다")
    void selfAidWithZeroMp() {
        // given: MP가 전혀 없는 객체를 준비한다.
        Cleric cleric = new Cleric("아서스", 40, 0);

        // when: selfAid 사용을 시도한다.
        cleric.selfAid();

        // then: 회복이 발생하지 않고 MP도 음수가 되지 않는지 검증한다.
        Assertions.assertEquals(40, cleric.hp);
        Assertions.assertEquals(0, cleric.mp);
    }

    @Test
    @DisplayName("HP가 0이어도 MP가 5이면 현재 구현상 회복할 수 있다")
    void selfAidWithZeroHp() {
        // given: HP 0과 사용 가능한 MP 5를 준비한다.
        // 사망 상태나 HP 0에서의 사용 금지는 과제에 정의되어 있지 않다.
        Cleric cleric = new Cleric("아서스", 0, 5);

        // when: selfAid를 사용한다.
        cleric.selfAid();

        // then: 현재 구현대로 HP가 50으로 회복되고 MP가 0인지 검증한다.
        Assertions.assertEquals(50, cleric.hp);
        Assertions.assertEquals(0, cleric.mp);
    }

    @Test
    @DisplayName("HP가 최대값 바로 아래인 49이면 50으로 회복한다")
    void selfAidWithFortyNineHp() {
        // given: 최대 HP 바로 아래인 HP 49와 MP 5를 준비한다.
        Cleric cleric = new Cleric("아서스", 49, 5);

        // when: selfAid를 사용한다.
        cleric.selfAid();

        // then: HP가 정확히 50이고 MP가 0인지 검증한다.
        Assertions.assertEquals(50, cleric.hp);
        Assertions.assertEquals(0, cleric.mp);
    }

    @Test
    @DisplayName("이미 HP가 50이어도 현재 구현상 MP를 5 소비한다")
    void selfAidWithMaximumHp() {
        // given: HP가 이미 최대인 50이고 MP가 5인 객체를 준비한다.
        Cleric cleric = new Cleric("아서스", 50, 5);

        // when: 최대 HP 상태에서 selfAid를 사용한다.
        cleric.selfAid();

        // then: 현재 정책대로 HP는 유지되고 MP는 5 소비되는지 검증한다.
        Assertions.assertEquals(50, cleric.hp);
        Assertions.assertEquals(0, cleric.mp);
    }

    @Test
    @DisplayName("MP 10에서 selfAid는 두 번 성공하고 세 번째에는 MP가 줄지 않는다")
    void selfAidRepeatedly() {
        // given: selfAid를 두 번 사용할 수 있는 MP 10을 준비한다.
        Cleric cleric = new Cleric("아서스", 40, 10);

        // when 1: 첫 번째 selfAid를 사용한다.
        cleric.selfAid();

        // then 1: HP가 회복되고 MP가 5 남는지 검증한다.
        Assertions.assertEquals(5, cleric.mp);
        Assertions.assertEquals(50, cleric.hp);

        // when 2: MP가 5인 상태에서 다시 selfAid를 사용한다.
        cleric.selfAid();

        // then 2: 남은 MP를 모두 소비했는지 검증한다.
        Assertions.assertEquals(0, cleric.mp);

        // when 3: MP가 0인 상태에서 세 번째 사용을 시도한다.
        cleric.selfAid();

        // then 3: 추가 소비 없이 MP 0과 HP 50을 유지하는지 검증한다.
        Assertions.assertEquals(0, cleric.mp);
        Assertions.assertEquals(50, cleric.hp);
    }

    // 5. pray 시간 경계: -1, 0, 1 및 대표값 2, 3

    @Test
    @DisplayName("-1초 기도하면 0을 반환하고 MP를 유지한다")
    void prayForNegativeSeconds() {
        // given: 회복할 공간이 있는 MP 5의 객체를 준비한다.
        Cleric cleric = new Cleric("아서스", 40, 5);

        // when: 유효하지 않은 시간 -1초로 기도한다.
        int recoveredMp = cleric.pray(-1);

        // then: 0을 반환하고 MP와 HP가 그대로인지 검증한다.
        Assertions.assertEquals(0, recoveredMp);
        Assertions.assertEquals(5, cleric.mp);
        Assertions.assertEquals(40, cleric.hp);
    }

    @Test
    @DisplayName("0초 기도하면 0을 반환하고 MP를 유지한다")
    void prayForZeroSeconds() {
        // given: 회복할 공간이 있는 MP 5의 객체를 준비한다.
        Cleric cleric = new Cleric("아서스", 40, 5);

        // when: 무효 시간의 경계인 0초로 기도한다.
        int recoveredMp = cleric.pray(0);

        // then: 0을 반환하고 MP와 HP가 그대로인지 검증한다.
        Assertions.assertEquals(0, recoveredMp);
        Assertions.assertEquals(5, cleric.mp);
        Assertions.assertEquals(40, cleric.hp);
    }

    @Test
    @DisplayName("최소 양수 시간인 1초 기도하면 1~3을 회복한다")
    void prayForOneSecond() {
        // given: 회복량이 최대 MP 제한에 걸리지 않도록 MP 0을 준비한다.
        Cleric cleric = new Cleric("아서스", 40, 0);

        // when: 최소 양수 시간인 1초로 기도한다.
        int recoveredMp = cleric.pray(1);

        // then: 1~3을 반환하고 같은 양만큼 MP가 증가했는지 검증한다.
        Assertions.assertTrue(recoveredMp >= 1 && recoveredMp <= 3);
        Assertions.assertEquals(recoveredMp, cleric.mp);
    }

    @Test
    @DisplayName("2초 기도하면 2~4를 회복한다")
    void prayForTwoSeconds() {
        // given: MP가 비어 있는 객체를 준비한다.
        Cleric cleric = new Cleric("아서스", 40, 0);

        // when: 일반 시간인 2초로 기도한다.
        int recoveredMp = cleric.pray(2);

        // then: 2~4를 반환하고 같은 양만큼 MP가 증가했는지 검증한다.
        Assertions.assertTrue(recoveredMp >= 2 && recoveredMp <= 4);
        Assertions.assertEquals(recoveredMp, cleric.mp);
    }

    // 6. pray의 기존 MP와 최대 MP 제한

    @Test
    @DisplayName("기존 MP가 5이면 3초 기도 후 MP는 8~10이다")
    void prayWithFiveMp() {
        // given: 최대 MP까지 5만큼 비어 있는 객체를 준비한다.
        Cleric cleric = new Cleric("아서스", 40, 5);

        // when: 3초 기도한다.
        int recoveredMp = cleric.pray(3);

        // then: 3~5를 기존 MP에 더하고 최대 MP를 넘지 않는지 검증한다.
        Assertions.assertTrue(recoveredMp >= 3 && recoveredMp <= 5);
        Assertions.assertEquals(5 + recoveredMp, cleric.mp);
        Assertions.assertTrue(cleric.mp <= 10);
    }

    @Test
    @DisplayName("기존 MP가 6이면 3초 기도의 실제 회복량은 3~4이다")
    void prayWithSixMp() {
        // given: 최대 MP까지 4만큼 비어 있는 객체를 준비한다.
        Cleric cleric = new Cleric("아서스", 40, 6);

        // when: 계산상 3~5를 회복하는 3초 기도를 실행한다.
        int recoveredMp = cleric.pray(3);

        // then: 실제 회복량은 3~4이고 최종 MP가 10 이하인지 검증한다.
        Assertions.assertTrue(recoveredMp >= 3 && recoveredMp <= 4);
        Assertions.assertEquals(6 + recoveredMp, cleric.mp);
        Assertions.assertTrue(cleric.mp <= 10);
        // 보정값을 통제하지 않으므로, 이 한 번에 초과 제한이 발생했는지는 보장하지 않는다.
    }

    @Test
    @DisplayName("기존 MP가 7이면 3초 기도로 정확히 3을 회복한다")
    void prayWithSevenMp() {
        // given: 최대 MP까지 3만큼 비어 있는 객체를 준비한다.
        Cleric cleric = new Cleric("아서스", 40, 7);

        // when: 3초 기도한다.
        int recoveredMp = cleric.pray(3);

        // then: 실제 회복량 3과 최종 MP 10을 검증한다.
        // 계산상 회복량은 최소 3이므로 어떤 보정값이어도 최대 MP에 도달한다.
        Assertions.assertEquals(3, recoveredMp);
        Assertions.assertEquals(10, cleric.mp);
    }

    @Test
    @DisplayName("기존 MP가 8이면 3초 기도로 실제 회복량 2만 반환한다")
    void prayWithEightMp() {
        // given: 최대 MP까지 2만큼 비어 있는 객체를 준비한다.
        Cleric cleric = new Cleric("아서스", 40, 8);

        // when: 3초 기도한다.
        int recoveredMp = cleric.pray(3);

        // then: 제한된 회복량 2를 반환하고 MP가 실제로 2 증가했는지 검증한다.
        // 계산상 회복량은 3~5지만 남은 공간은 2다. 반드시 초과 제한이 발생한다.
        Assertions.assertEquals(2, recoveredMp);
        Assertions.assertEquals(10, cleric.mp);
        Assertions.assertEquals(cleric.mp - 8, recoveredMp);
    }

    @Test
    @DisplayName("최대 MP 바로 아래인 9에서는 실제 회복량이 1이다")
    void prayWithNineMp() {
        // given: 최대 MP 바로 아래인 MP 9를 준비한다.
        Cleric cleric = new Cleric("아서스", 40, 9);

        // when: 3초 기도한다.
        int recoveredMp = cleric.pray(3);

        // then: 남은 공간만큼 1을 반환하고 MP가 10인지 검증한다.
        Assertions.assertEquals(1, recoveredMp);
        Assertions.assertEquals(10, cleric.mp);
    }

    @Test
    @DisplayName("이름만 지정해 MP 10으로 생성한 직후 기도하면 회복량이 0이다")
    void prayWithMaximumMp() {
        // given: 이름만 지정하여 HP 50, MP 10인 객체를 준비한다.
        Cleric cleric = new Cleric("아서스");

        // when: MP가 가득 찬 상태에서 3초 기도한다.
        int recoveredMp = cleric.pray(3);

        // then: 회복량은 0이고 MP와 HP가 그대로인지 검증한다.
        Assertions.assertEquals(0, recoveredMp);
        Assertions.assertEquals(10, cleric.mp);
        Assertions.assertEquals(50, cleric.hp);
    }

    @Test
    @DisplayName("MP 0에서 10초 기도해도 최대 MP인 10까지만 회복한다")
    void prayForTenSeconds() {
        // given: MP가 비어 있는 객체를 준비한다.
        Cleric cleric = new Cleric("아서스", 40, 0);

        // when: 계산상 회복량이 10~12인 10초 기도를 실행한다.
        int recoveredMp = cleric.pray(10);

        // then: 최대 MP까지만 회복하고 실제 회복량 10을 반환하는지 검증한다.
        Assertions.assertEquals(10, recoveredMp);
        Assertions.assertEquals(10, cleric.mp);
    }

    @Test
    @DisplayName("아주 큰 양수 시간에도 MP가 음수로 넘치지 않는다")
    void prayForMaximumSeconds() {
        // given: MP 0인 객체를 준비한다.
        // 앞서 발견한 int 덧셈 오버플로를 방지한 코드의 보충 검사다.
        Cleric cleric = new Cleric("아서스", 40, 0);

        // when: int의 최댓값을 기도 시간으로 전달한다.
        int recoveredMp = cleric.pray(Integer.MAX_VALUE);

        // then: 음수로 넘치지 않고 회복량과 최종 MP가 모두 10인지 검증한다.
        Assertions.assertEquals(10, recoveredMp);
        Assertions.assertEquals(10, cleric.mp);
    }

    // 7. 객체를 생성하고 행동을 이어서 실행하는 흐름

    @Test
    @DisplayName("selfAid 후 3초 기도하면 소비한 MP를 3~5만큼 회복한다")
    void selfAidThenPray() {
        // given: 기본 HP 50, MP 10인 객체를 준비한다.
        Cleric cleric = new Cleric("아서스");

        // when 1: selfAid를 사용하여 MP를 소비한다.
        cleric.selfAid();

        // then 1: 기도 전 상태가 HP 50, MP 5인지 검증한다.
        Assertions.assertEquals(50, cleric.hp);
        Assertions.assertEquals(5, cleric.mp);

        // when 2: MP 5인 상태에서 3초 기도한다.
        int recoveredMp = cleric.pray(3);

        // then 2: 회복량이 기존 MP에 더해지고 HP·이름은 유지되는지 검증한다.
        Assertions.assertTrue(recoveredMp >= 3 && recoveredMp <= 5);
        Assertions.assertEquals(5 + recoveredMp, cleric.mp);
        Assertions.assertEquals(50, cleric.hp);
        Assertions.assertEquals("아서스", cleric.name);
    }
}
