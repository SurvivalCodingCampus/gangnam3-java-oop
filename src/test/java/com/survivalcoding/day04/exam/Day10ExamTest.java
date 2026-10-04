package com.survivalcoding.day04.exam;

import com.survivalcoding.day10.exam.Book;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.*;
import java.util.Collections;
import static org.assertj.core.api.Assertions.*;

public class Day10ExamTest {
    @Nested
    @DisplayName("날짜 포맷 테스트")
    class DateFormatTest {
        @ParameterizedTest
        @ValueSource(strings = { "2019-08-11-10", "2026-07-11-11" })
        @DisplayName("정상 날짜 통과")
        void test2(String dateStr) {
            assertThatNoException()
                    .isThrownBy(() -> new Book("책", dateStr, "책입니다"));
        }

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
                "2026-02-29-23", // 2월은 28일까지
                "2024-02-30-23"  // 윤일 29일까지 있음
        })
        @DisplayName("비정상 날짜가 들어오면 예외")
        void test1(String dateStr) {
            assertThatThrownBy(() -> new Book("책", dateStr, "책입니다"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("올바른 날짜 포맷을 넣어주세요");
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualTest {
        @ParameterizedTest
        @CsvSource({
                "책, 2019-08-11-20, 책, 2019-08-11-23",
                "책, 2026-07-11-10, 책, 2026-07-11-18"
        })
        @DisplayName("책 제목이 같고 yyyy-MM-dd까지만 같으면 같음")
        void test3(String title1, String dateStr1, String title2, String dateStr2) {
            Book book1 = new Book(title1, dateStr1, "책입니다");
            Book book2 = new Book(title2, dateStr2, "책입니다");

            assertThat(book1).isEqualTo(book2);
        }

        @ParameterizedTest
        @CsvSource({
                "책, 2019-08-11-20, 책1, 2019-08-11-23", // 제목 다름
                "책, 2026-07-11-10, 책책, 2026-07-11-18", // 제목 다름
                "책, 2026-07-11-10, 책, 2026-07-12-18", // 날짜 다름
                "책, 2026-07-10-10, 책, 2026-07-11-18", // 날짜 다름
        })
        @DisplayName("책 제목이 다르거나 yyyy-MM-dd가 다르면 틀림")
        void test5(String title1, String dateStr1, String title2, String dateStr2) {
            Book book1 = new Book(title1, dateStr1, "책입니다");
            Book book2 = new Book(title2, dateStr2, "책입니다");

            // equals() 검증 대칭성 포함
            assertThat(book1).isNotEqualTo(book2);
            assertThat(book1).isNotEqualTo(book2);
        }

        @ParameterizedTest
        @CsvSource({
                "책, 2019-08-11-20, 책, 2019-08-11-23",
                "책, 2026-07-11-10, 책, 2026-07-11-18"
        })
        @DisplayName("책 제목이 같고 yyyy-MM-dd까지만 같으면 같음(hashcode)")
        void test13(String title1, String dateStr1, String title2, String dateStr2) {
            Book book1 = new Book(title1, dateStr1, "책입니다");
            Book book2 = new Book(title2, dateStr2, "책입니다");

            // equals() 검증 대칭성 포함
            assertThat(book1).isEqualTo(book2);
            assertThat(book2).isEqualTo(book1);

            // equals가 true이면 hashCode도 반드시 같아야 함
            assertThat(book1.hashCode()).isEqualTo(book2.hashCode());
        }

        @Test
        @DisplayName("equals의 기본 규약 검증")
        void testEqualsContract() {
            Book book = new Book("책", "2026-07-11-10", "책입니다");

            // 자기 자신과의 비교
            assertThat(book).isEqualTo(book);

            // null 또는 다른 객체와의 비교 시 false 반환 (NPE 없이 안전해야 함)
            assertThat(book).isNotEqualTo(null);
            assertThat(book).isNotEqualTo("String Object");
        }

        @ParameterizedTest
        @CsvSource({
                "책, 2019-08-11-20, 책1, 2019-08-11-23", // 제목 다름
                "책, 2026-07-11-10, 책책, 2026-07-11-18", // 제목 다름
                "책, 2026-07-11-10, 책, 2026-07-12-18", // 날짜 다름
                "책, 2026-07-10-10, 책, 2026-07-11-18", // 날짜 다름
        })
        @DisplayName("책 제목이 다르거나 yyyy-MM-dd가 다르면 틀림(hashCode)")
        void test22(String title1, String dateStr1, String title2, String dateStr2) {
            Book book1 = new Book(title1, dateStr1, "책입니다");
            Book book2 = new Book(title2, dateStr2, "책입니다");

            assertThat(book1.hashCode()).isNotEqualTo(book2.hashCode());
        }
    }


    @Nested
    @DisplayName("자료구조 테스트")
    class BookCollectionTest {
        @ParameterizedTest
        @CsvSource({
                "2019-08-11-20, 2019-08-11-11",
                "2026-07-11-20, 2026-07-11-02"
        })
        @DisplayName("동등하면 리스트에서 삭제 가능")
        void test5(String dateStr1, String dateStr2) {
            Book book1 = new Book("책", dateStr1, "책입니다");
            Book book2 = new Book("책", dateStr2, "책입니다");

            List<Book> books = new ArrayList<>();

            books.add(book1);
            books.remove(book2);

            assertThat(books).isEmpty();
        }

        @ParameterizedTest
        @CsvSource({
                "책, 2019-08-11-20, 책1, 2019-08-11-23", // 제목 다름
                "책, 2026-07-11-10, 책책, 2026-07-11-18", // 제목 다름
                "책, 2026-07-11-10, 책, 2026-07-12-18", // 날짜 다름
                "책, 2026-07-10-10, 책, 2026-07-11-18", // 날짜 다름
        })
        @DisplayName("동등하지 않으면 리스트에서 삭제 불가")
        void test111(String title1, String dateStr1, String title2, String dateStr2) {
            Book book1 = new Book(title1, dateStr1, "책입니다");
            Book book2 = new Book(title2, dateStr2, "책입니다");

            List<Book> books = new ArrayList<>();

            books.add(book1);
            books.remove(book2);

            assertThat(books).isNotEmpty();
        }

        @ParameterizedTest
        @CsvSource({
                "2019-08-11-22, 2019-08-11-13",
                "2026-07-11-23, 2026-07-11-02"
        })
        @DisplayName("동등하면 셋에서 삭제 가능")
        void test6(String dateStr1, String dateStr2) {
            Book book1 = new Book("책", dateStr1, "책입니다");
            Book book2 = new Book("책", dateStr2, "책입니다");

            Set<Book> books = new HashSet<>();

            books.add(book1);
            books.remove(book2);

            assertThat(books).isEmpty();
        }

        @ParameterizedTest
        @CsvSource({
                "책, 2019-08-11-20, 책1, 2019-08-11-23", // 제목 다름
                "책, 2026-07-11-10, 책책, 2026-07-11-18", // 제목 다름
                "책, 2026-07-11-10, 책, 2026-07-12-18", // 날짜 다름
                "책, 2026-07-10-10, 책, 2026-07-11-18", // 날짜 다름
        })
        @DisplayName("동등하지 않으면 셋에서 삭제 불가")
        void test5_notEqual(String title1, String dateStr1, String title2, String dateStr2) {
            Book book1 = new Book(title1, dateStr1, "책입니다");
            Book book2 = new Book(title2, dateStr2, "책입니다");

            Set<Book> books = new HashSet<>();

            books.add(book1);
            books.remove(book2);

            assertThat(books).isNotEmpty();
        }

        @ParameterizedTest
        @CsvSource({
                "2019-08-11-22, 2019-08-11-11",
                "2026-07-11-23, 2026-07-11-22"
        })
        @DisplayName("동등하면 맵에서 삭제 가능")
        void test7(String dateStr1, String dateStr2) {
            Book book1 = new Book("책", dateStr1, "책입니다");
            Book book2 = new Book("책", dateStr2, "책입니다");

            Map<Book, String> bookMap = new HashMap<>();
            bookMap.put(book1, "재고 있음");

            bookMap.remove(book2);

            assertThat(bookMap).isEmpty();
        }

        @ParameterizedTest
        @CsvSource({
                "책, 2019-08-11-20, 책1, 2019-08-11-23", // 제목 다름
                "책, 2026-07-11-10, 책책, 2026-07-11-18", // 제목 다름
                "책, 2026-07-11-10, 책, 2026-07-12-18", // 날짜 다름
                "책, 2026-07-10-10, 책, 2026-07-11-18", // 날짜 다름
        })
        @DisplayName("동등하지 않으면 맵에서 삭제 불가")
        void test5adsl(String title1, String dateStr1, String title2, String dateStr2) {
            Book book1 = new Book(title1, dateStr1, "책입니다");
            Book book2 = new Book(title2, dateStr2, "책입니다");

            Map<Book, String> bookMap = new HashMap<>();
            bookMap.put(book1, "재고");

            bookMap.remove(book2);

            assertThat(bookMap).isNotEmpty();
        }
    }

    @Test
    @DisplayName("신상순서로 정렬된다")
    void test2() {
        List<Book> books = Arrays.asList(
                new Book("책", "2026-10-11-21", "책입니다"),
                new Book("책", "2026-10-13-23", "책입니다"),
                new Book("책", "2026-10-12-20", "책입니다"),
                new Book("책", "2026-10-11-14", "책입니다")
        );

        Collections.sort(books);

        // getPublishDate를 기준으로 정렬됐는지 확인
        assertThat(books).isSortedAccordingTo(
                Comparator.comparing(
                        Book::getPublishDate,
                        Comparator.reverseOrder()
                )
        );
    }

    @Test
    @DisplayName("책을 클론하면 깊은 복사로 된다")
    void test2123312() {
        Book originalBook = new Book("책", "2019-08-11-20", "책입니다");
        Book copiedBook = originalBook.clone();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(copiedBook).isNotSameAs(originalBook);
            softly.assertThat(copiedBook.getPublishDate()).isNotSameAs(originalBook.getPublishDate());
        });
    }
}