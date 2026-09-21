package com.survivalcoding.day04_calss_instance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {
    
    // ==========================================
    // validateNotNull 테스트
    // ==========================================
    @Test
    @DisplayName("NotNull: null 입력 시 예외 발생")
    void validateNotNullThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Validator.validateNotNull(null)
        );
        assertEquals("null이 아니어야 함", exception.getMessage());
    }
    
    @Test
    @DisplayName("NotNull: null이 아닌 객체 입력 시 통과 (동등 분할)")
    void validateNotNullPasses() {
        assertDoesNotThrow(() -> Validator.validateNotNull(new Object()));
        assertDoesNotThrow(() -> Validator.validateNotNull("문자열"));
    }
    
    // ==========================================
    // validateMinLength 테스트
    // ==========================================
    @ParameterizedTest(name = "길이가 {1}일 때 이름 ''{0}''은(는) 예외 발생")  // {} 속은 파라미터의 인덱스
    @CsvSource({
            "가, 1",    // 길이 1, 최소길이 1 (이하) -> 예외
            "가, 2",    // 길이 1, 최소길이 2 (이하) -> 예외
            "가나, 2",  // 길이 2, 최소길이 2 (이하) -> 예외
            "'', 0"     // 빈 문자열 길이 0, 최소길이 0 (이하) -> 예외
    })
    @DisplayName("MinLength: 최소 길이 이하일 때 예외 발생 (경계값 분석)")
    void validateMinLengthThrowsException(String name, int minLength) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Validator.validateMinLength(name, minLength)
        );
        assertEquals("이름은 " + minLength + " 글자를 초과해야 함", exception.getMessage());
    }
    
    @ParameterizedTest(name = "길이가 {1}일 때 이름 ''{0}''은(는) 통과")
    @CsvSource({
            "가나, 1",    // 길이 2, 최소길이 1 -> 통과
            "가나다, 2"   // 길이 3, 최소길이 2 -> 통과
    })
    @DisplayName("MinLength: 최소 길이 초과일 때 통과 (경계값 분석)")
    void validateMinLengthPasses(String name, int minLength) {
        assertDoesNotThrow(() -> Validator.validateMinLength(name, minLength));
    }
    
    // ==========================================
    // validateMaxLength 테스트
    // ==========================================
    @ParameterizedTest(name = "최대 길이가 {1}일 때 이름 ''{0}''은(는) 예외 발생")
    @CsvSource({
            "가나, 2",    // 길이 2, 최대길이 2 (이상) -> 예외
            "가나다, 2",  // 길이 3, 최대길이 2 (이상) -> 예외
            "가, 1"      // 길이 1, 최대길이 1 (이상) -> 예외
    })
    @DisplayName("MaxLength: 최대 길이 이상일 때 예외 발생 (경계값 분석)")
    void validateMaxLengthThrowsException(String name, int maxLength) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Validator.validateMaxLength(name, maxLength)
        );
        assertEquals("이름은 " + maxLength + " 글자 미만이어야 함", exception.getMessage());
    }
    
    @ParameterizedTest(name = "최대 길이가 {1}일 때 이름 ''{0}''은(는) 통과")
    @CsvSource({
            "가, 2",    // 길이 1, 최대길이 2 -> 통과
            "가나, 3"   // 길이 2, 최대길이 3 -> 통과
    })
    @DisplayName("MaxLength: 최대 길이 미만일 때 통과 (경계값 분석)")
    void validateMaxLengthPasses(String name, int maxLength) {
        assertDoesNotThrow(() -> Validator.validateMaxLength(name, maxLength));
    }
    
    // ==========================================
    // validateAtLeast 테스트
    // ==========================================
    @ParameterizedTest(name = "값 {0}이 최소값 {1} 미만일 때 예외 발생")
    @CsvSource({
            "-0.1, 0.0",
            "9.9, 10.0",
            "0.49, 0.5"
    })
    @DisplayName("AtLeast: 최소값 미만일 때 예외 발생 (경계값 분석)")
    void validateAtLeastThrowsException(double value, double min) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Validator.validateAtLeast(value, min)
        );
        assertEquals(String.format("값은 %f 이상이어야 함", min), exception.getMessage());
    }
    
    @ParameterizedTest(name = "값 {0}이 최소값 {1} 이상일 때 통과")
    @CsvSource({
            "0.0, 0.0",    // 경계값 (동일)
            "0.1, 0.0",    // 경계값 초과
            "10.0, 10.0",  // 경계값 (동일)
            "50.0, 0.5"    // 충분히 큰 값 (동등 분할)
    })
    @DisplayName("AtLeast: 최소값 이상일 때 통과 (경계값 분석 및 동등 분할)")
    void validateAtLeastPasses(double value, double min) {
        assertDoesNotThrow(() -> Validator.validateAtLeast(value, min));
    }
    
    // ==========================================
    // validateAtMost 테스트
    // ==========================================
    @ParameterizedTest(name = "값 {0}이 최대값 {1} 초과일 때 예외 발생")
    @CsvSource({
            "0.1, 0.0",
            "10.1, 10.0",
            "100.01, 100.0"
    })
    @DisplayName("AtMost: 최대값 초과일 때 예외 발생 (경계값 분석)")
    void validateAtMostThrowsException(double value, double max) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Validator.validateAtMost(value, max)
        );
        assertEquals(String.format("값은 %f 이하여야 함", max), exception.getMessage());
    }
    
    @ParameterizedTest(name = "값 {0}이 최대값 {1} 이하일 때 통과")
    @CsvSource({
            "0.0, 0.0",      // 경계값 (동일)
            "-0.1, 0.0",     // 경계값 미만
            "10.0, 10.0",    // 경계값 (동일)
            "50.0, 100.0"    // 충분히 작은 값 (동등 분할)
    })
    @DisplayName("AtMost: 최대값 이하일 때 통과 (경계값 분석 및 동등 분할)")
    void validateAtMostPasses(double value, double max) {
        assertDoesNotThrow(() -> Validator.validateAtMost(value, max));
    }
    
    // ==========================================
    // validateRangeInclusive 테스트
    // ==========================================
    @ParameterizedTest(name = "값 {0}이 범위 [{1}, {2}]를 벗어날 때 예외 발생")
    @CsvSource({
            "0.49, 0.5, 100.0",   // 하한값 미만
            "100.01, 0.5, 100.0"  // 상한값 초과
    })
    @DisplayName("RangeInclusive: 지정된 범위를 벗어날 때 예외 발생 (경계값 분석)")
    void validateRangeInclusiveThrowsException(double value, double min, double max) {
        assertThrows(
                IllegalArgumentException.class,
                () -> Validator.validateRangeInclusive(value, min, max)
        );
    }
    
    @ParameterizedTest(name = "값 {0}이 범위 [{1}, {2}] 내에 있을 때 통과")
    @CsvSource({
            "0.5, 0.5, 100.0",    // 하한 경계값
            "50.0, 0.5, 100.0",   // 중간값 (동등 분할)
            "100.0, 0.5, 100.0"   // 상한 경계값
    })
    @DisplayName("RangeInclusive: 지정된 범위 내에 있을 때 통과 (경계값 분석 및 동등 분할)")
    void validateRangeInclusivePasses(double value, double min, double max) {
        assertDoesNotThrow(() -> Validator.validateRangeInclusive(value, min, max));
    }
}