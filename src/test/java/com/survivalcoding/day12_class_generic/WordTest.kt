package com.survivalcoding.day12_class_generic

// JUnit의 검증 함수와 테스트 표시용 애너테이션을 가져온다.
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Word의 판별 결과와 인덱스 사용 조건을 검증한다.
 * println으로 결과를 보는 예제와 달리, 기대값이 다르면 테스트가 실패한다.
 *
 * @Test는 Gradle/JUnit이 실행할 테스트 함수를 표시한다.
 * 백틱으로 함수 이름을 감싸면 공백을 포함한 한국어 설명을 이름으로 사용할 수 있다.
 *
 * 각 테스트의 word는 검사하는 동안 다른 객체로 재할당하지 않으므로 val이다.
 * 반복문의 i도 직접 변경하지 않고 문자열의 indices에서 순서대로 제공받는다.
 * 후보 목록은 구현에서 가져오지 않고 테스트에서 정해 결과를 독립적으로 확인한다.
 *
 * assertTrue: 결과가 true여야 한다. assertFalse: 결과가 false여야 한다.
 * 두 번째 인자의 메시지는 실패한 글자를 알려 주어 원인을 찾기 쉽게 한다.
 * assertThrows: 중괄호 안의 코드를 실행했을 때 지정한 타입의 예외가 나야 한다.
 *
 * 실행: ./gradlew test --tests com.survivalcoding.day12_class_generic.WordTest
 */
class WordTest {

    @Test
    fun `소문자와 대문자 모음 다섯 가지를 모음으로 판단한다`() {
        // a 하나만 검사하면 나머지 모음을 빠뜨린 구현을 발견할 수 없다.
        // 모음 다섯 가지와 대문자를 모두 넣어 빠진 후보와 대소문자 처리 오류를 확인한다.
        val word = Word("aeiouAEIOU")

        // 각 글자의 유효한 인덱스를 순회하고, 모든 글자가 true인지 각각 검증한다.
        for (i in word.word.indices) {
            assertTrue(word.isVowel(i), "'${word.word[i]}'는 모음이어야 한다")
        }
    }

    @Test
    fun `소문자와 대문자 자음을 자음으로 판단한다`() {
        // y도 이 문제의 a/e/i/o/u 모음 목록에 없으므로 자음으로 판단한다.
        // 영어 자음 21개를 대소문자 모두 검사한다.
        val word = Word("bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ")

        for (i in word.word.indices) {
            assertTrue(word.isConsonant(i), "'${word.word[i]}'는 자음이어야 한다")
        }
    }

    @Test
    fun `모음은 자음으로 판단하지 않는다`() {
        // 자음 판별을 알파벳 범위 검사만으로 구현하는 오류를 찾아낸다.
        val word = Word("aeiouAEIOU")

        for (i in word.word.indices) {
            assertFalse(word.isConsonant(i), "'${word.word[i]}'는 자음이 아니다")
        }
    }

    @Test
    fun `자음은 모음으로 판단하지 않는다`() {
        // 모음 함수가 모든 알파벳에 true를 반환하는 오류를 찾아낸다.
        val word = Word("bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ")

        for (i in word.word.indices) {
            assertFalse(word.isVowel(i), "'${word.word[i]}'는 모음이 아니다")
        }
    }

    @Test
    fun `숫자 공백 기호 한글은 영어 모음도 자음도 아니다`() {
        // !isVowel(i)만으로 자음을 판별하면 이 글자들이 자음으로 처리된다.
        // 영어 알파벳 확인이 제대로 적용되는지 두 함수의 false 결과를 함께 검사한다.
        val word = Word("0123456789 !?@#가나다")

        for (i in word.word.indices) {
            assertFalse(word.isVowel(i), "'${word.word[i]}'는 영어 모음이 아니다")
            assertFalse(word.isConsonant(i), "'${word.word[i]}'는 영어 자음이 아니다")
        }
    }

    @Test
    fun `인덱스는 0부터 시작하며 해당 위치의 글자를 판단한다`() {
        // 서로 다른 글자가 섞인 단어로 요청한 위치를 실제로 읽는지 확인한다.
        // 항상 첫 글자만 검사하는 오류나 인덱스를 하나씩 밀어서 읽는 오류를 잡을 수 있다.
        val word = Word("Hello")

        // 0: H는 자음, 1: e는 모음, 2와 3: l은 자음, 4: o는 모음이다.
        assertFalse(word.isVowel(0))
        assertTrue(word.isConsonant(0))
        assertTrue(word.isVowel(1))
        assertFalse(word.isConsonant(1))
        assertFalse(word.isVowel(2))
        assertTrue(word.isConsonant(2))
        assertFalse(word.isVowel(3))
        assertTrue(word.isConsonant(3))
        assertTrue(word.isVowel(4))
        assertFalse(word.isConsonant(4))
    }

    @Test
    fun `문자열 범위를 벗어난 인덱스는 예외를 발생시킨다`() {
        val word = Word("Hello")

        // 길이가 5이므로 유효한 위치는 0..4이다. -1과 5는 양쪽 경계 밖의 위치이다.
        // 잘못된 위치를 false로 숨기지 않고 예외로 알리는 현재 사용 조건을 검증한다.
        for (i in listOf(-1, 5)) {
            // ::class.java는 assertThrows에 전달할 예외 클래스 정보를 얻는다.
            // { ... }는 함수 호출을 담은 람다이다. JUnit이 실행하고 예외를 확인한다.
            assertThrows(IndexOutOfBoundsException::class.java) { word.isVowel(i) }
            assertThrows(IndexOutOfBoundsException::class.java) { word.isConsonant(i) }
        }
    }

    @Test
    fun `빈 문자열은 확인할 글자가 없어 예외를 발생시킨다`() {
        val word = Word("")

        // 빈 문자열에는 0번째 글자도 없다. 직접 호출하면 두 함수 모두 예외가 발생한다.
        // WordMain처럼 indices를 순회하면 빈 문자열에서는 호출 자체가 발생하지 않는다.
        assertThrows(IndexOutOfBoundsException::class.java) { word.isVowel(0) }
        assertThrows(IndexOutOfBoundsException::class.java) { word.isConsonant(0) }
    }
}
