package com.survivalcoding.day05_class_instance;

import java.time.Year;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PersonTest {

    @Test
    @DisplayName("[Person] 2000년생의 이름·출생 연도를 저장하고 나이는 현재 연도에서 출생 연도를 뺀 값이다")
    void createPersonBornIn2000() {
        // given: 출생 연도 2000, 현재 연도 Year.now().getValue()을 준비한다.
        int birthYear = 2000;
        int currentYear = Year.now().getValue();

        // when: Person 생성자을 실행한다.
        Person person = new Person("홍길동", birthYear);

        // then: 2000년생의 이름·출생 연도를 저장하고 나이는 현재 연도에서 출생 연도를 뺀 값이다
        Assertions.assertEquals("홍길동", person.getName());
        Assertions.assertEquals(birthYear, person.getBirthYear());
        Assertions.assertEquals(currentYear - birthYear, person.getAge());
    }

    @Test
    @DisplayName("[Person] 출생 연도 1을 허용하고 나이는 현재 연도에서 출생 연도를 뺀 값이다")
    void createPersonBornInYearOne() {
        // given: 출생 연도 1, 현재 연도 Year.now().getValue()을 준비한다.
        int birthYear = 1;
        int currentYear = Year.now().getValue();

        // when: Person 생성자을 실행한다.
        Person person = new Person("홍길동", birthYear);

        // then: 출생 연도 1을 허용하고 나이는 현재 연도에서 출생 연도를 뺀 값이다
        Assertions.assertEquals("홍길동", person.getName());
        Assertions.assertEquals(birthYear, person.getBirthYear());
        Assertions.assertEquals(currentYear - birthYear, person.getAge());
    }

    @Test
    @DisplayName("[Person] 올해 태어난 사람을 생성하면 나이는 0이다")
    void createPersonBornThisYear() {
        // given: 출생 연도 Year.now().getValue(), 현재 연도 Year.now().getValue()을 준비한다.
        int birthYear = Year.now().getValue();
        int currentYear = Year.now().getValue();

        // when: Person 생성자을 실행한다.
        Person person = new Person("홍길동", birthYear);

        // then: 올해 태어난 사람을 생성하면 나이는 0이다
        Assertions.assertEquals("홍길동", person.getName());
        Assertions.assertEquals(birthYear, person.getBirthYear());
        Assertions.assertEquals(currentYear - birthYear, person.getAge());
    }

    @Test
    @DisplayName("[Person] 출생 연도에 0을 전달하면 IllegalArgumentException이 발생한다")
    void rejectZeroBirthYear() {
        // given: 출생 연도 0을 준비한다.
        int birthYear = 0;

        // when: Person 생성자을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Person("홍길동", birthYear));

        // then: 출생 연도에 0을 전달하면 IllegalArgumentException이 발생한다
        Assertions.assertNotNull(error.getMessage());
    }

    @Test
    @DisplayName("[Person] 출생 연도에 -1을 전달하면 IllegalArgumentException이 발생한다")
    void rejectNegativeBirthYear() {
        // given: 출생 연도 -1을 준비한다.
        int birthYear = -1;

        // when: Person 생성자을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Person("홍길동", birthYear));

        // then: 출생 연도에 -1을 전달하면 IllegalArgumentException이 발생한다
        Assertions.assertNotNull(error.getMessage());
    }

    @Test
    @DisplayName("[Person] 출생 연도에 올해 + 1을 전달하면 IllegalArgumentException이 발생한다")
    void rejectFutureBirthYear() {
        // given: 출생 연도 Year.now().getValue() + 1을 준비한다.
        int birthYear = Year.now().getValue() + 1;

        // when: Person 생성자을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Person("홍길동", birthYear));

        // then: 출생 연도에 올해 + 1을 전달하면 IllegalArgumentException이 발생한다
        Assertions.assertNotNull(error.getMessage());
    }

    @Test
    @DisplayName("[Person] 출생 연도에 연도 대신 날짜 형식인 19000101을 전달하면 IllegalArgumentException이 발생한다")
    void rejectDateFormattedBirthYear() {
        // given: 출생 연도 19000101을 준비한다.
        int birthYear = 19000101;

        // when: Person 생성자을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Person("홍길동", birthYear));

        // then: 출생 연도에 연도 대신 날짜 형식인 19000101을 전달하면 IllegalArgumentException이 발생한다
        Assertions.assertNotNull(error.getMessage());
    }

    @Test
    @DisplayName("[Person] null으로 사람을 생성하면 IllegalArgumentException이 발생한다")
    void rejectNullName() {
        // given: 이름 null을 준비한다.
        String name = null;

        // when: Person 생성자을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Person(name, 2000));

        // then: null으로 사람을 생성하면 IllegalArgumentException이 발생한다
        Assertions.assertNotNull(error.getMessage());
    }

    @Test
    @DisplayName("[Person] 1글자 이름(김)으로 사람을 생성하면 IllegalArgumentException이 발생한다")
    void rejectOneCharacterName() {
        // given: 이름 "김"을 준비한다.
        String name = "김";

        // when: Person 생성자을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Person(name, 2000));

        // then: 1글자 이름(김)으로 사람을 생성하면 IllegalArgumentException이 발생한다
        Assertions.assertNotNull(error.getMessage());
    }

    @Test
    @DisplayName("[Person] 공백만 2개인 이름으로 사람을 생성하면 IllegalArgumentException이 발생한다")
    void rejectBlankName() {
        // given: 이름 "  "을 준비한다.
        String name = "  ";

        // when: Person 생성자을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Person(name, 2000));

        // then: 공백만 2개인 이름으로 사람을 생성하면 IllegalArgumentException이 발생한다
        Assertions.assertNotNull(error.getMessage());
    }

    @Test
    @DisplayName("[Person] 기존 추가 규칙의 경계인 2글자 이름 허용")
    void acceptTwoCharacterName() {
        // given: 이름 "이름"을 준비한다.
        String name = "이름";

        // when: Person 생성자을 실행한다.
        Person person = new Person(name, 2000);

        // then: 기존 추가 규칙의 경계인 2글자 이름 허용
        Assertions.assertEquals(name, person.getName());
    }

    // 게임 사용 흐름, 상태 전이, 객체 관계와 실패 후 재시도 검사

    @Test
    @DisplayName("[Person] 서로 다른 사람을 생성해도 기존 사람의 정보는 변하지 않는다")
    void differentPeopleKeepIndependentProfiles() {
        // given: first = new Person("홍길동", 2000)을 준비한다.
        Person first = new Person("홍길동", 2000);

        // when: Person 생성자을 실행한다.
        Person second = new Person("김영희", 1995);

        // then: 서로 다른 사람을 생성해도 기존 사람의 정보는 변하지 않는다
        Assertions.assertNotSame(first, second);
        Assertions.assertEquals("홍길동", first.getName());
        Assertions.assertEquals(2000, first.getBirthYear());
        Assertions.assertEquals("김영희", second.getName());
        Assertions.assertEquals(1995, second.getBirthYear());
    }

    @Test
    @DisplayName("[Person] 이름과 출생 연도가 같아도 별개의 사람 객체다")
    void sameNamesDoNotMergePeople() {
        // given: first = new Person("홍길동", 2000)을 준비한다.
        Person first = new Person("홍길동", 2000);

        // when: Person 생성자을 실행한다.
        Person second = new Person("홍길동", 2000);

        // then: 이름과 출생 연도가 같아도 별개의 사람 객체다
        Assertions.assertNotSame(first, second);
        Assertions.assertEquals(first.getName(), second.getName());
        Assertions.assertEquals(first.getBirthYear(), second.getBirthYear());
    }

    @Test
    @DisplayName("[Person] 잘못된 등록 이후 올바른 입력으로 다시 등록할 수 있다")
    void correctInputAfterFailedRegistration() {
        // given: 이름 "홍길동"을 준비한다.
        String name = "홍길동";

        // when: Person 생성자을 실행한다. 잘못된 입력의 예외를 함께 검증한다.
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Person(name, 0));
        Person person = new Person(name, 2000);

        // then: 잘못된 등록 이후 올바른 입력으로 다시 등록할 수 있다
        Assertions.assertEquals(name, person.getName());
        Assertions.assertEquals(2000, person.getBirthYear());
    }

    @Test
    @DisplayName("[Person] 같은 해에 조회한 두 사람의 나이 차는 출생 연도 차와 같다")
    void ageDifferenceMatchesBirthYearDifference() {
        // given: 조회 전 연도 Year.now().getValue()을 준비한다.
        Person older = new Person("김영희", 1990);
        Person younger = new Person("홍길동", 2000);
        int beforeYear = Year.now().getValue();

        // when: older.getAge(), younger.getAge()을 실행한다.
        int olderAge = older.getAge();
        int youngerAge = younger.getAge();
        int afterYear = Year.now().getValue();

        // then: 같은 해에 조회한 두 사람의 나이 차는 출생 연도 차와 같다
        // 연도가 바뀌는 순간에는 비교 전제가 성립하지 않으므로 이 검사만 건너뛴다.
        org.junit.jupiter.api.Assumptions.assumeTrue(beforeYear == afterYear);
        Assertions.assertEquals(10, olderAge - youngerAge);
    }

    @Test
    @DisplayName("[Person] 프로필을 반복 조회해도 이름과 출생 연도는 유지된다")
    void readingProfileDoesNotChangeIt() {
        // given: person = new Person("홍길동", 2000)을 준비한다.
        Person person = new Person("홍길동", 2000);

        // when: person.getName(), person.getBirthYear(), person.getAge()을 실행한다.
        for (int i = 0; i < 10; i++) {
            person.getName();
            person.getBirthYear();
            person.getAge();
        }

        // then: 프로필을 반복 조회해도 이름과 출생 연도는 유지된다
        Assertions.assertEquals("홍길동", person.getName());
        Assertions.assertEquals(2000, person.getBirthYear());
    }

    @Test
    @DisplayName("[Person] 유효한 이름의 앞뒤 공백과 가운데 공백은 저장 시 유지된다")
    void preserveWhitespaceName() {
        // given: 이름 "  홍 길동  "을 준비한다.
        String name = "  홍 길동  ";

        // when: Person 생성자을 실행한다.
        Person subject = new Person(name, 2000);

        // then: 유효한 이름의 앞뒤 공백과 가운데 공백은 저장 시 유지된다
        Assertions.assertEquals(name, subject.getName());
    }

    @Test
    @DisplayName("[Person] 영문 이름을 사용할 수 있다")
    void preserveEnglishName() {
        // given: 이름 "Merlin"을 준비한다.
        String name = "Merlin";

        // when: Person 생성자을 실행한다.
        Person subject = new Person(name, 2000);

        // then: 영문 이름을 사용할 수 있다
        Assertions.assertEquals(name, subject.getName());
    }
}
