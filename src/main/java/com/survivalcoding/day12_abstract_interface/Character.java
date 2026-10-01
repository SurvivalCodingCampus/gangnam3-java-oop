package com.survivalcoding.day12_abstract_interface;

/**
 * 추상 클래스(abstract class) —— 상속의 재료로 쓰이지만, 그 자체로는 인스턴스화할 수 없는 클래스.
 * <p>
 * 이 클래스가 답한 질문은 하나다.
 * <strong>"상속의 재료를 만드는 개발자는, 미래의 개발자가 이 클래스를 어떻게 써도 안전한 코드를 어떻게 만들 수 있는가?"</strong>
 *
 * <h2>왜 그냥 클래스가 아니라 추상 클래스인가</h2>
 * 일반 클래스로 만들고 {@code attack()} 을 빈 메서드로 두면 문제가 생긴다.
 * <pre>
 * // 이 버전의 문제점
 * public class Character {
 *     public void attack() {
 *         // 비어 있음 — 자식이 언제든 잊어버려도 컴파일이 된다
 *     }
 * }
 * </pre>
 * 빈 메서드는 "오버라이드 안 해도 되는 것"으로 컴파일러에게 말해버린다.
 * 그러면 미래의 개발자가 {@code Slime} 을 만들면서 {@code attack()} 을 잊어버리고,
 * 조용히 아무 일도 일어나지 않는 객체가 만들어진다. 버그는 실행해봐야 안다.
 * <p>
 * 또 다른 위험이 있다. {@code attack} 이라는 철자를 실수로 {@code atack} 으로 적어버리면,
 * 그 메서드는 "오버라이드할 메서드"가 아니라 "완전히 새로운 메서드"가 된다.
 * 컴파일러는 아무 말 없이 통과시키고, 사람은 그 클래스가 정상인 줄 믿는다.
 *
 * <h2>추상 메서드가 해결하는 것</h2>
 * {@code abstract} 를 붙이면 컴파일러가 두 가지를 강제한다.
 * <ol>
 *     <li><b>오버라이드 강제</b> — 자식은 {@code attack()} 을 반드시 구현해야 한다.
 *         빠뜨리면 <b>컴파일 에러</b>가 난다. 버그가 실행되지 않고 개발 단계에서 잡힌다.</li>
 *     <li><b>인스턴스화 금지</b> — 상세 구현이 정해지지 않은 클래스를 {@code new} 로 만들 수 없다.
 *         실수로 만들어 버리는 일이 원천적으로 막힌다.</li>
 * </ol>
 *
 * <h2>이 클래스의 사용법</h2>
 * 직접 만들지 않는다. 상속의 재료로만 쓴다.
 * <pre>
 * Monster monster = new Monster("고블린", 30);   // 가능 (상속한 구체 클래스)
 * // Character c = new Character("이름");        // 불가능 — 컴파일 에러
 * </pre>
 *
 * <h2>이름을 정할 때 주의할 점</h2>
 * {@code Character} 는 {@code java.lang.Character} (char 를 감싸는 래퍼 클래스)와
 * <b>이름이 겹친다.</b> 그래서 이 클래스를 다른 패키지에서 쓸 때
 * <pre>
 * import com.survivalcoding.day12_abstract_interface.*;   // 와일드카드 import
 * </pre>
 * 로 불러오면 {@code java.lang.*} 과 충돌해 컴파일 에러가 난다.
 * <pre>
 * error: reference to Character is ambiguous
 * </pre>
 * 해결 방법은 두 가지다.
 * <ul>
 *     <li>{@code Character} 하나만 정확히 import 한다 (와일드카드 대신)</li>
 *     <li>이름을 바꾼다. 예: {@code BaseCharacter}, {@code GameCharacter}</li>
 * </ul>
 * 같은 패키지 안에서는 모호함이 없어 그대로 쓰지만, 다른 패키지에서 쓸 때는 위를 기억하자.
 * 수업에서 다루는 이름이라 그대로 두었지만, 실제 프로젝트에서는 이런 <b>이름 충돌</b>을
 * 피하는 편이 좋다. (JDK 의 {@code Character}, {@code String}, {@code Integer} 가 같은 유형)
 *
 * @see Monster
 * @see com.survivalcoding.day12_abstract_interface.Wizard
 */
public abstract class Character {

    /** 캐릭터의 이름. 자식에게 상속되어 계속 쓰이는 공통 데이터. */
    private final String name;

    /**
     * 이름을 받는 생성자.
     * <p>
     * {@code final} 을 붙였으므로 이름은 한 번 정해지면 바뀌지 않는다.
     * 캐릭터의 이름은 생성 시점에 확정된다고 본 것이고, {@code setName} 을 두지 않은 이유다.
     *
     * @param name 캐릭터의 이름
     */
    public Character(String name) {
        this.name = name;
    }

    /**
     * 캐릭터의 이름을 돌려준다.
     * <p>
     * {@code name} 은 {@code private} 이므로 외부에서 직접 접근할 수 없다.
     * 자식 클래스도 이 getter 를 통해서만 이름을 얻는다. (private 은 자식에게 열려 있지 않다)
     *
     * @return 캐릭터의 이름
     */
    public String getName() {
        return name;
    }

    /**
     * 공격한다 —— 상세 정의가 정해지지 않은 추상 메서드.
     * <p>
     * 여기에는 구현이 없다. 대신 "이 메서드는 반드시 존재하고, 자식이 반드시 채운다"라는
     * <b>약속만</b> 남긴다. 본문({@code {}}) 대신 세미콜론만 쓴다는 점이 일반 메서드와의 차이다.
     * <p>
     * 자식 클래스가 이 메서드를 정의하지 않으면 컴파일 에러:
     * <pre>
     * // Monster must implement the inherited abstract method Character.attack()
     * </pre>
     *
     * @return 이 메서드는 값을 돌려주지 않는다 (void)
     */
    public abstract void attack();
}