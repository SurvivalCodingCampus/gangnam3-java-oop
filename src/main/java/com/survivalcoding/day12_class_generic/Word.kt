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
    fun isVowel(i: Int): Boolean =
        word[i].lowercaseChar() in "aeiou"

    // 연습문제 2: 영어 알파벳이면서 모음이 아닌 글자를 자음으로 판단한다.
    fun isConsonant(i: Int): Boolean {
        val ch = word[i].lowercaseChar()
        return ch in 'a'..'z' && ch !in "aeiou"
    }
}
