package com.survivalcoding.day10_class_instance_book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    @DisplayName("[Book] 생성자와 getter로 제목, 출간일, 설명을 확인한다")
    void constructorStoresFields() {
        // given (준비)
        Date publishDate = date("2024-01-01T00:00:00");

        // when (실행)
        Book book = new Book("Java 입문", publishDate, "첫 번째 설명");

        // then (검증)
        assertEquals("Java 입문", book.getTitle());
        assertEquals(publishDate, book.getPublishDate());
        assertEquals("첫 번째 설명", book.getComment());
    }

    @Test
    @DisplayName("[Book] 기본 생성 후 setter로 각 필드를 설정한다")
    void settersUpdateFields() {
        // given (준비)
        Book book = new Book();
        Date publishDate = date("2024-01-01T00:00:00");

        // when (실행)
        book.setTitle("Java 입문");
        book.setPublishDate(publishDate);
        book.setComment("새로운 설명");

        // then (검증)
        assertEquals("Java 입문", book.getTitle());
        assertEquals(publishDate, book.getPublishDate());
        assertEquals("새로운 설명", book.getComment());
    }

    @Test
    @DisplayName("[Book] 주소와 설명이 달라도 제목과 출간일이 같으면 같은 책이다")
    void sameTitleAndDateAreEqual() {
        // given (준비)
        Book first = new Book(new String("Java 입문"), date("2024-01-01T00:00:00"), "설명 A");
        Book second = new Book(new String("Java 입문"), date("2024-01-01T00:00:00"), "설명 B");

        // when (실행)
        boolean equal = first.equals(second);

        // then (검증)
        assertNotSame(first, second);
        assertTrue(equal);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    @DisplayName("[Book] 같은 출간일의 시각 차이는 동등성, 해시값, 정렬에 영향을 주지 않는다")
    void publicationDayIgnoresTime() {
        // given (준비)
        Book morning = new Book("Java 입문", date("2024-01-01T00:00:00"), "오전");
        Book evening = new Book("Java 입문", date("2024-01-01T23:59:59"), "오후");

        // when (실행)
        int comparison = morning.compareTo(evening);

        // then (검증)
        assertEquals(morning, evening);
        assertEquals(morning.hashCode(), evening.hashCode());
        assertEquals(0, comparison);
    }

    @Test
    @DisplayName("[Book] 제목이 다르면 다른 책이다")
    void differentTitlesAreNotEqual() {
        // given (준비)
        Book java = new Book("Java 입문", date("2024-01-01T00:00:00"), "설명");
        Book kotlin = new Book("Kotlin 입문", date("2024-01-01T00:00:00"), "설명");

        // then (검증)
        assertNotEquals(java, kotlin);
    }

    @Test
    @DisplayName("[Book] 출간일이 다르면 다른 책이다")
    void differentDatesAreNotEqual() {
        // given (준비)
        Book first = new Book("Java 입문", date("2024-01-01T23:59:59"), "설명");
        Book second = new Book("Java 입문", date("2024-01-02T00:00:00"), "설명");

        // then (검증)
        assertNotEquals(first, second);
    }

    @Test
    @DisplayName("[Book] equals는 자기 자신, 대칭성, 추이성, 반복 비교 규칙을 만족한다")
    void equalsSatisfiesContract() {
        // given (준비)
        Book first = new Book("Java 입문", date("2024-01-01T00:00:00"), "A");
        Book second = new Book("Java 입문", date("2024-01-01T12:00:00"), "B");
        Book third = new Book("Java 입문", date("2024-01-01T23:59:59"), "C");

        // then (검증)
        assertEquals(first, first);
        assertEquals(first, second);
        assertEquals(second, first);
        assertEquals(second, third);
        assertEquals(first, third);
        assertEquals(first, second);
        assertEquals(first.hashCode(), first.hashCode());
    }

    @Test
    @DisplayName("[Book] null이나 다른 타입의 객체와는 동등하지 않다")
    void nullAndOtherTypesAreNotEqual() {
        // given (준비)
        Book book = new Book("Java 입문", date("2024-01-01T00:00:00"), "설명");

        // then (검증)
        assertFalse(book.equals(null));
        assertFalse(book.equals("Java 입문"));
    }

    @Test
    @DisplayName("[Book] List에서 동등한 다른 인스턴스로 검색하고 삭제한다")
    void listFindsAndRemovesEquivalentBook() {
        // given (준비)
        Book stored = new Book("Java 입문", date("2024-01-01T00:00:00"), "저장한 책");
        Book equivalent = new Book("Java 입문", date("2024-01-01T12:00:00"), "검색용 책");
        List<Book> books = new ArrayList<>(List.of(stored));

        // then (검증)
        assertTrue(books.contains(equivalent));
        assertEquals(0, books.indexOf(equivalent));
        assertTrue(books.remove(equivalent));
        assertTrue(books.isEmpty());
    }

    @Test
    @DisplayName("[Book] HashSet은 동등한 책의 중복을 제거하고 다른 인스턴스로 삭제한다")
    void setDeduplicatesAndRemovesEquivalentBook() {
        // given (준비)
        Book first = new Book("Java 입문", date("2024-01-01T00:00:00"), "A");
        Book second = new Book("Java 입문", date("2024-01-01T23:59:59"), "B");
        Set<Book> books = new HashSet<>();

        // when (실행)
        assertTrue(books.add(first));
        assertFalse(books.add(second));

        // then (검증)
        assertEquals(1, books.size());
        assertTrue(books.contains(second));
        assertTrue(books.remove(second));
        assertTrue(books.isEmpty());
    }

    @Test
    @DisplayName("[Book] HashMap은 동등한 책을 같은 키로 조회하고 값을 갱신한다")
    void mapFindsAndReplacesEquivalentKey() {
        // given (준비)
        Book first = new Book("Java 입문", date("2024-01-01T00:00:00"), "A");
        Book second = new Book("Java 입문", date("2024-01-01T12:00:00"), "B");
        Map<Book, String> locations = new HashMap<>();
        locations.put(first, "A 서가");

        // when (실행)
        assertEquals("A 서가", locations.get(second));
        String previous = locations.put(second, "B 서가");

        // then (검증)
        assertEquals("A 서가", previous);
        assertEquals(1, locations.size());
        assertEquals("B 서가", locations.get(first));
        assertEquals("B 서가", locations.remove(second));
        assertTrue(locations.isEmpty());
    }

    @Test
    @DisplayName("[Book] HashSet은 제목이나 출간일이 다른 책을 각각 저장한다")
    void setPreservesDifferentBooks() {
        // given (준비)
        Book java = new Book("Java 입문", date("2024-01-01T00:00:00"), "A");
        Book kotlin = new Book("Kotlin 입문", date("2024-01-01T00:00:00"), "B");
        Book nextEdition = new Book("Java 입문", date("2025-01-01T00:00:00"), "C");

        // when (실행)
        Set<Book> books = new HashSet<>(List.of(java, kotlin, nextEdition));

        // then (검증)
        assertEquals(3, books.size());
        assertTrue(books.containsAll(List.of(java, kotlin, nextEdition)));
    }

    @Test
    @DisplayName("[Book] Collections.sort는 최신 출간일 순으로 정렬한다")
    void collectionsSortOrdersNewestFirst() {
        // given (준비)
        Book oldest = new Book("A 책", date("2023-01-01T00:00:00"), "구간");
        Book middle = new Book("B 책", date("2024-06-01T00:00:00"), "중간");
        Book newest = new Book("C 책", date("2025-01-01T00:00:00"), "신간");
        List<Book> books = new ArrayList<>(List.of(middle, oldest, newest));

        // when (실행)
        Collections.sort(books);

        // then (검증)
        assertEquals(List.of(newest, middle, oldest), books);
    }

    @Test
    @DisplayName("[Book] compareTo는 최신 책에 음수, 오래된 책에 양수, 같은 책에 0을 반환한다")
    void compareToReturnsCorrectSigns() {
        // given (준비)
        Book older = new Book("Java 입문", date("2024-01-01T00:00:00"), "구간");
        Book newer = new Book("Java 입문", date("2025-01-01T00:00:00"), "신간");
        Book equivalent = new Book("Java 입문", date("2025-01-01T12:00:00"), "다른 설명");

        // then (검증)
        assertTrue(newer.compareTo(older) < 0);
        assertTrue(older.compareTo(newer) > 0);
        assertEquals(0, newer.compareTo(equivalent));
        assertEquals(0, newer.compareTo(newer));
    }

    @Test
    @DisplayName("[Book] 같은 출간일에는 제목 순으로 정렬한다")
    void sameDateUsesTitleAsTieBreaker() {
        // given (준비)
        Book firstTitle = new Book("A 책", date("2024-01-01T00:00:00"), "A");
        Book secondTitle = new Book("B 책", date("2024-01-01T23:59:59"), "B");
        List<Book> books = new ArrayList<>(List.of(secondTitle, firstTitle));

        // when (실행)
        Collections.sort(books);

        // then (검증)
        assertEquals(List.of(firstTitle, secondTitle), books);
        assertTrue(firstTitle.compareTo(secondTitle) < 0);
        assertNotEquals(firstTitle, secondTitle);
    }

    @Test
    @DisplayName("[Book] 출간일이 없으면 마지막에, 같은 날짜에 제목이 없으면 먼저 정렬한다")
    void sortHandlesMissingFields() {
        // given (준비)
        Book undated = new Book("미출간", null, "설명");
        Book titled = new Book("Java 입문", date("2024-01-01T00:00:00"), "설명");
        Book untitled = new Book(null, date("2024-01-01T00:00:00"), "설명");
        List<Book> books = new ArrayList<>(List.of(undated, titled, untitled));

        // when (실행)
        Collections.sort(books);

        // then (검증)
        assertEquals(List.of(untitled, titled, undated), books);
    }

    @Test
    @DisplayName("[Book] null인 책 자체와의 정렬 비교는 예외가 발생한다")
    void compareToRejectsNullBook() {
        // given (준비)
        Book book = new Book("Java 입문", date("2024-01-01T00:00:00"), "설명");

        // then (검증)
        assertThrows(NullPointerException.class, () -> book.compareTo(null));
    }

    @Test
    @DisplayName("[Book] clone은 값이 같고 책과 날짜의 주소는 다른 복사본을 반환한다")
    void cloneCopiesBookAndDate() {
        // given (준비)
        Book original = new Book("Java 입문", date("2024-01-01T12:34:56"), "원본 설명");

        // when (실행)
        Book copy = original.clone();

        // then (검증)
        assertNotSame(original, copy);
        assertNotSame(original.getPublishDate(), copy.getPublishDate());
        assertEquals(original, copy);
        assertEquals(original.hashCode(), copy.hashCode());
        assertEquals(original.getTitle(), copy.getTitle());
        assertEquals(original.getPublishDate(), copy.getPublishDate());
        assertEquals(original.getComment(), copy.getComment());
    }

    @Test
    @DisplayName("[Book] 복사본의 Date 내부 값을 변경해도 원본은 바뀌지 않는다")
    void changingCloneDateDoesNotChangeOriginal() {
        // given (준비)
        Date originalDate = date("2024-01-01T00:00:00");
        Book original = new Book("Java 입문", originalDate, "원본 설명");
        Book copy = original.clone();
        Date changedDate = date("2025-02-02T00:00:00");

        // when (실행)
        // 날짜 객체를 직접 바꿔서 깊은 복사인지 확인
        copy.getPublishDate().setTime(changedDate.getTime());

        // then (검증)
        assertEquals(date("2024-01-01T00:00:00"), original.getPublishDate());
        assertEquals(changedDate, copy.getPublishDate());
        assertNotEquals(original, copy);
    }

    @Test
    @DisplayName("[Book] 원본의 Date 내부 값을 변경해도 복사본은 바뀌지 않는다")
    void changingOriginalDateDoesNotChangeClone() {
        // given (준비)
        Book original = new Book("Java 입문", date("2024-01-01T00:00:00"), "설명");
        Book copy = original.clone();
        Date changedDate = date("2023-03-03T00:00:00");

        // when (실행)
        original.getPublishDate().setTime(changedDate.getTime());

        // then (검증)
        assertEquals(changedDate, original.getPublishDate());
        assertEquals(date("2024-01-01T00:00:00"), copy.getPublishDate());
    }

    @Test
    @DisplayName("[Book] 복사본의 제목과 설명을 바꾸어도 원본의 문자열 필드는 보존된다")
    void changingCloneStringsDoesNotChangeOriginal() {
        // given (준비)
        Book original = new Book("Java 입문", date("2024-01-01T00:00:00"), "원본 설명");
        Book copy = original.clone();

        // when (실행)
        copy.setTitle("Java 응용");
        copy.setComment("복사본 설명");

        // then (검증)
        assertEquals("Java 입문", original.getTitle());
        assertEquals("원본 설명", original.getComment());
        assertEquals("Java 응용", copy.getTitle());
        assertEquals("복사본 설명", copy.getComment());
    }

    @Test
    @DisplayName("[Book] 미정인 필드도 동등성 비교, 해시값 계산, 복사가 가능하다")
    void defaultBookCanBeComparedAndCloned() {
        // given (준비)
        Book original = new Book();

        // when (실행)
        Book copy = original.clone();

        // then (검증)
        assertNotSame(original, copy);
        assertEquals(original, copy);
        assertEquals(original.hashCode(), copy.hashCode());
        assertEquals(0, original.compareTo(copy));
        assertNull(copy.getTitle());
        assertNull(copy.getPublishDate());
        assertNull(copy.getComment());
        assertNotEquals(original, new Book("제목 있음", null, null));
        assertNotEquals(original, new Book(null, date("2024-01-01T00:00:00"), null));
    }

    @Test
    @DisplayName("[Book] toString은 제목, 연·월·일 출간일, 설명을 보여 준다")
    void toStringShowsBookFields() {
        // given (준비)
        Book book = new Book("Java 입문", date("2024-01-01T12:34:56"), "학습용 책");

        // when (실행)
        String text = book.toString();

        // then (검증)
        assertEquals("Book{title='Java 입문', publishDate=2024-01-01, comment='학습용 책'}", text);
    }

    // 테스트용 날짜 생성
    private static Date date(String dateTime) {
        return Date.from(LocalDateTime.parse(dateTime)
                .atZone(ZoneId.systemDefault()).toInstant());
    }
}
