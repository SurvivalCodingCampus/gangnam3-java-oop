package com.survivalcoding.day12_class_generic

// main은 실행 예제의 시작점이다. 판별 규칙은 Word에 두고 여기서는 결과를 출력한다.
fun main() {
    // Word("Hello")로 검사할 문자열을 보관하는 객체를 생성한다.
    // 다른 Word 객체를 다시 대입하지 않으므로 지역 변수도 val을 사용한다.
    // 이 지역 변수 word와 Word 내부의 word 프로퍼티는 서로 다른 선언이다.
    // val인 참조가 객체 내부의 변경까지 막는 것은 아니지만,
    // 이 Word 클래스의 문자열 프로퍼티 역시 val이므로 재대입할 수 없다.
    val word = Word("Hello")

    // 첫 번째 word는 위의 객체, 두 번째 .word는 객체가 보관한 문자열이다.
    // indices는 0부터 마지막 글자 인덱스까지의 범위를 제공한다.
    // "Hello"라면 0..4이며, 빈 문자열이라면 반복문이 실행되지 않는다.
    // for (i in ...)의 in은 오른쪽 값을 순회하는 반복문 문법이다.
    // isVowel에서 쓴 '포함 여부를 판별하는 in'과 문맥이 다르다.
    // i는 매 반복에 제공되는 인덱스이다. 직접 i++할 필요도, var로 선언할 필요도 없다.
    for (i in word.word.indices) {
        // when은 위에서부터 조건을 확인하고 처음 true인 가지의 값을 반환한다.
        // 그 반환값이 String이므로 type은 String으로 추론된다.
        // 매 반복마다 새로 초기화하고 이후 재대입하지 않으므로 val이다.
        // 모음도 자음도 아닌 글자는 else에서 처리해 출력할 값을 항상 정한다.
        val type = when {
            word.isVowel(i) -> "모음"
            word.isConsonant(i) -> "자음"
            else -> "영어 알파벳 아님"
        }

        // 문자열 보간: $type은 변수, ${...}는 계산식이나 프로퍼티 접근 결과를 삽입한다.
        // 내부 인덱스는 0부터 시작하지만 출력은 사람이 읽기 편하게 i + 1로 표시한다.
        // word.word[i]는 실제 글자를 가져오고 println은 완성한 문자열을 한 줄 출력한다.
        println("${i + 1}번째 글자 '${word.word[i]}': $type")
    }
}
