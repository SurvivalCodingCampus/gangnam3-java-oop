package com.survivalcoding.day04_operation_instance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Book 클래스 테스트")
public class BookTest {

    @Test
    @DisplayName("제목과 출판일이 같으면 서로 다른 인스턴스여도 같은 책이어야 한다")
    void equals_withSameTitleAndDate_shouldReturnTrue() throws ParseException {
        // given
        Book book1 = new Book("혼자 공부하는 자바", DateUtil.toDate("2024-02-01"), "혼자 공부하는 자바");
        Book book2 = new Book("혼자 공부하는 자바", DateUtil.toDate("2024-02-01"), "혼공자");

        // when
        boolean result = book1.equals(book2);

        // then
        assertFalse(book1 == book2);
        assertTrue(result);
        assertEquals(book1.hashCode(), book2.hashCode());
    }

    @Test
    @DisplayName("제목과 출판일이 같은 책은 List에서 동등한 책으로 찾을 수 있어야 한다")
    void arrayList_withEqualBooks_shouldContain() throws ParseException {
        // given
        Book book1 = new Book("혼자 공부하는 자바", DateUtil.toDate("2024-02-01"), "혼자 공부하는 자바");
        Book book2 = new Book("혼자 공부하는 자바", DateUtil.toDate("2024-02-01"), "혼공자");
        List<Book> books = Arrays.asList(book1);

        // when
        boolean result = books.contains(book2);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("제목과 출판일이 같은 책은 Set에 하나만 저장되어야 한다")
    void hashSet_withEqualBooks_shouldContainOnlyOne() throws ParseException {
        // given
        Book book1 = new Book("혼자 공부하는 자바", DateUtil.toDate("2024-02-01"), "혼자 공부하는 자바");
        Book book2 = new Book("혼자 공부하는 자바", DateUtil.toDate("2024-02-01"), "혼공자");
        Set<Book> books = new HashSet<>();

        // when
        books.add(book1);
        books.add(book2);

        // then
        assertEquals(1, books.size());
    }

    @Test
    @DisplayName("제목과 출판일이 같은 책은 Map에 한 쌍만 저장되어야 한다")
    void hashMap_withEqualBooks_shouldContainOnlyOne() throws ParseException {
        // given
        Book book1 = new Book("혼자 공부하는 자바", DateUtil.toDate("2024-02-01"), "혼자 공부하는 자바");
        Book book2 = new Book("혼자 공부하는 자바", DateUtil.toDate("2024-02-01"), "혼공자");
        Map<Book, String> books = new HashMap<>();

        // when
        books.put(book1, book1.getComment());
        books.put(book2, book2.getComment());

        // then
        assertEquals(1, books.size());
    }

    @Test
    @DisplayName("Collections.sort()를 하면 출판일이 최신인 책부터 정렬되어야 한다")
    void sort_shouldOrderByNewestPublishDate() throws ParseException {
        // given
        Book book1 = new Book("혼자 공부하는 자바", DateUtil.toDate("2024-02-01"), "혼자 공부하는 자바");
        Book book2 = new Book("이펙티브 자바", DateUtil.toDate("2018-11-01"), "이펙티브 자바");
        Book book3 = new Book("이펙티브 코틀린", DateUtil.toDate("2025-06-09"), "이펙티브 코틀린");
        List<Book> books = new ArrayList<>(List.of(book1, book3, book2));

        // when
        Collections.sort(books);

        // then
        assertEquals(book3, books.get(0));
        assertEquals(book1, books.get(1));
        assertEquals(book2, books.get(2));
    }

    @Test
    @DisplayName("clone()은 원본과 같은 값을 가진 독립된 인스턴스를 반환해야 한다")
    void clone_shouldReturnIndependentCopy() throws ParseException {
        // given
        Book book = new Book("혼자 공부하는 자바", DateUtil.toDate("2024-02-01"), "혼자 공부하는 자바");

        // when
        Book bookCopy = book.clone();
        book.setPublishDate(DateUtil.toDate("2018-11-01"));

        // then
        assertFalse(book == bookCopy);
        assertEquals(DateUtil.toDate("2024-02-01"), bookCopy.getPublishDate());
    }
}
