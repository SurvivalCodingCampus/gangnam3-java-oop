package com.survivalcoding.day04_operation_instance;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DateUtil 클래스 테스트")
public class DateUtilTest {

    @Test
    @DisplayName("존재하지 않는 날짜는 ParseException이 발생해야 한다")
    void toDate_withInvalidDate_shouldThrow() {
        assertThrows(ParseException.class, () -> DateUtil.toDate("2024-02-30"));
    }

    @Test
    @DisplayName("날짜 뒤에 다른 문자열이 있으면 ParseException이 발생해야 한다")
    void toDate_withTrailingText_shouldThrow() {
        assertThrows(ParseException.class, () -> DateUtil.toDate("2024-02-01abc"));
    }
}
