package com.survivalcoding.day04.exam;

import com.survivalcoding.day10.exam.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.ValueSources;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.*;

public class Day10ExamTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "-111-11-22-20",
            "0-11-22-20",
            "2019-111-22-20",
            "2019-11-221-20",
            "2000-0-22-20",
            "2000-13-02-20",
            "2000-10-10-80",
            "2000-1-34-10",
    })
    @DisplayName("비정상 날짜가 들어오면 예외")
    void test1(String dateStr) {
        assertThatThrownBy(() -> new Book("책", dateStr, "책입니다"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바른 날짜 포맷을 넣어주세요");
    }

    @ParameterizedTest
    @ValueSource(strings = { "2019-08-11-33", "2026-07-11-22" })
    @DisplayName("정상 날짜 통과")
    void test2(String dateStr) {
        assertThatNoException()
                .isThrownBy(() -> new Book("책", dateStr, "책입니다"));
    }

    @ParameterizedTest
    @CsvSource({
            "2019-08-11-20, 2019-08-11-30",
            "2026-07-11-10, 2026-07-11-50"
    })
    @DisplayName("같은 yyyy-MM-dd까지만 같으면 같음")
    void test3(String dateStr1, String dateStr2) {
        Book book1 = new Book("책", dateStr1, "책입니다");
        Book book2 = new Book("책", dateStr2, "책입니다");

        assertThat(book1).isEqualTo(book2);
    }
}
