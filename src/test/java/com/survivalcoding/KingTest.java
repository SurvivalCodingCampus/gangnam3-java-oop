package com.survivalcoding;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link King} 클래스의 테스트.
 * <p>
 * 왕은 용사와 다른 계층의 객체이므로 상속이 아니라 <b>합성(composition)</b> 관계다.
 * {@code King} 은 {@code Hero} 를 필드로 갖고, {@code Hero} 타입의 매개변수를 받는다.
 * 이 테스트는 왕이 용사를 "부르는" 상호작용이 잘 동작하는지 확인한다.
 * <p>
 * 참고: {@code callHero} 과 {@code talk} 은 public 이 아니라 default(패키지) 접근이므로,
 * 이 테스트는 반드시 {@code com.survivalcoding} 패키지에 있어야 실행된다.
 */
class KingTest {

    /**
     * {@code System.out} 을 잠시 가로채어 출력된 내용을 돌려주는 헬퍼.
     *
     * @param action 출력을 발생시킬 동작
     * @return 해당 동작이 출력한 문자열
     */
    private String captureOutput(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try (PrintStream capture = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            action.run();
        } finally {
            System.setOut(original);
        }

        return buffer.toString(StandardCharsets.UTF_8);
    }

    @Test
    @DisplayName("왕은 용사가 아니다 — 합성 관계이지 상속 관계가 아니다")
    void kingIsNotAHero() {
        King king = new King();

        // 상속이 아니라면 instanceof 가 false 여야 한다.
        // 왕이 용사를 "갖고" 있을 뿐, 용사 "이므로"는 아니다.
        //
        // 컴파일러는 King 과 Hero 가 형제 계층의 무관한 타입이라는 걸 정적으로 알고
        // `king instanceof Hero` 를 컴파일 에러로 막는다. 런타임 값으로도 확인하려고
        // Object 로 한 단계 올려 검사한다.
        Object kingAsObject = king;

        assertFalse(kingAsObject instanceof Hero);
    }

    @Test
    @DisplayName("callHero 는 용사를 welcomes 하고 용사의 정보를 출력한 뒤 작별을 고한다")
    void callHeroWelcomesAndFarewells() {
        Hero hero = new Hero("한석봉", 100);
        King king = new King();

        String output = captureOutput(() -> king.callHero(hero));

        assertTrue(output.contains("용사님, 저희 왕국에 와주셔서 감사합니다"));
        assertTrue(output.contains("용사님의 이름은한석봉이고, hp는 100입니다"));
        // 마지막에 용사가 직접 인사하는 bye() 가 호출된다.
        assertTrue(output.contains("용자는 이별을 고했다. 빠이"));
    }

    @Test
    @DisplayName("callHero 는 용사의 HP를 바꾸지 않는다")
    void callHeroDoesNotChangeHp() {
        Hero hero = new Hero("한석봉", 100);
        King king = new King();

        captureOutput(() -> king.callHero(hero));

        assertEquals(100, hero.getHp());
    }

    @Test
    @DisplayName("talk 는 용사에게 인사를 건네고 용사가 도망가게 한다")
    void talkMakesHeroRunAway() {
        Hero hero = new Hero("한석봉", 100);
        King king = new King();

        String output = captureOutput(() -> king.talk(hero));

        assertTrue(output.contains("왕 : 우리 성에 어서오시오. 용사한석봉이여"));
        assertTrue(output.contains("왕 : 긴 여행에 피로하겠군"));
        // 마지막에 hero.run() 이 호출되어 용사가 도망간다.
        assertTrue(output.contains("한석봉는 도망쳤다!"));
        assertTrue(output.contains("GAME OVER"));
    }
}