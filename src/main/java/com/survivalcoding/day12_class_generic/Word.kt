// 기존 예제와 같은 패키지에 두어 WordMain과 테스트에서 Word를 사용할 수 있다.
package com.survivalcoding.day12_class_generic

/**
 * 영어 모음과 자음을 판별하는 클래스. 숫자, 공백, 한글 등은 둘 다 false이다.
 *
 * val과 var의 선택 기준:
 * - val: 초기화한 뒤 다른 값으로 재할당할 필요가 없을 때 사용한다.
 * - var: 실행 중 다른 값으로 재할당할 필요가 있을 때 사용한다.
 * 이 문제에서는 생성할 때 받은 문자열을 읽기만 하므로 val을 사용한다.
 * 강의의 var도 사용할 수 있지만, 현재 기능에는 문자열을 바꿀 필요가 없다.
 * 나중에 같은 Word 객체의 문자열을 바꾸는 요구가 생기면 var로 바꿀 수 있다.
 * 예: var라면 wordObject.word = "World"처럼 새 문자열을 대입할 수 있다.
 *
 * String 자체는 불변이다. var여도 기존 String의 글자를 직접 수정하는 것이 아니라
 * 변수가 다른 String을 가리키게 된다. lowercase()도 원본을 수정하지 않는다.
 * val은 재할당을 막는 선언이며, 모든 객체의 내부 상태까지 불변으로 만들지는 않는다.
 * 예를 들어 val로 선언한 MutableList는 add()로 내용을 바꿀 수 있다.
 *
 * 인덱스 i는 0부터 시작하며, 0 이상 word.length 미만이어야 한다.
 * 잘못된 인덱스는 word[i]에서 IndexOutOfBoundsException이 발생한다.
 * 호출하는 쪽에서 word.indices를 사용하면 유효한 위치만 순회할 수 있다.
 */
class Word(val word: String) {
    // 생성자의 val word는 객체가 보관하는 읽기 전용 프로퍼티이다.
    // 함수 매개변수 i도 함수 안에서 재할당할 수 없다. Int는 위치, Boolean은 판별 결과이다.

    // 연습문제 1: i번째 글자가 모음인지 확인한다.
    // word[i]는 한 글자를 Char로 가져온다. substring(i, i + 1)은 String을 반환하므로
    // 한 글자만 판별하는 여기서는 인덱스로 바로 가져오는 편이 목적에 맞다.
    // substring으로 한 글자짜리 중간 문자열을 만드는 단계도 생략할 수 있다.
    // lowercaseChar()는 Char를 소문자 Char로 변환해 'A'와 'a'를 동일하게 판단한다.
    // 기존 word의 내용은 바뀌지 않는다.
    //
    // ch in "aeiou"는 "aeiou".contains(ch)와 같은 포함 검사이다.
    // ch == 'a' || ch == 'e' || ...처럼 다섯 조건을 나열하는 것보다
    // 모음 후보를 한곳에 모아 보여 주므로 읽고 수정하기 쉽다.
    // in의 주된 장점은 가독성이다. contains()보다 더 빠른 별도 연산은 아니다.
    // 반환식이 하나라서 중괄호와 return 대신 '= 식' 형태의 함수로 작성한다.
    fun isVowel(i: Int): Boolean =
        word[i].lowercaseChar() in "aeiou"

    // 연습문제 2: 영어 알파벳이면서 모음이 아닌 글자를 자음으로 판단한다.
    fun isConsonant(i: Int): Boolean {
        // ch는 이 호출에서 한 번 계산한 글자를 보관한다. 다시 대입하지 않으므로 val이다.
        // 타입은 반환값에서 Char로 추론되므로 ': Char'를 생략할 수 있다.
        // 소문자로 변환해 알파벳 범위 검사와 모음 검사에서 같은 값을 사용한다.
        val ch = word[i].lowercaseChar()

        // 작은따옴표는 Char, 큰따옴표는 String이다.
        // 'a'..'z'는 양 끝을 포함하는 문자 범위이고, in은 그 범위에 속하는지 확인한다.
        // !in은 포함되지 않는지 확인하며 !"aeiou".contains(ch)와 같은 의미이다.
        // &&는 두 조건이 모두 true일 때만 true이다.
        // 왼쪽 조건이 false이면 오른쪽 조건을 평가하지 않는 단락 평가도 수행한다.
        // 단순히 !isVowel(i)만 사용하면 숫자나 공백도 자음으로 오인하므로
        // 영어 알파벳 범위부터 확인해야 한다.
        // 이미 구한 ch를 재사용하면 isVowel(i)를 다시 호출할 때의
        // 글자 읽기와 소문자 변환을 반복하지 않아도 된다.
        return ch in 'a'..'z' && ch !in "aeiou"
    }
}
