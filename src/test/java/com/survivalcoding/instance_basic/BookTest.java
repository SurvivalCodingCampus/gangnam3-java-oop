package com.survivalcoding.instance_basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    @Test
    @DisplayName("제목과 출간일(예: 2024-01–01)이 같으면 같은 책으로 판단한다. 또한 List, Map, Set 등의 컬렉션에 넣어도 동일 객체로 판단한다.")
    void equalsTest() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date1 = sdf.parse("2024-01-01 14:30:00");    // 미친듯한 성능의 pc에서 무진장 빠르게 2개 코드가 실행되면서
        Date date2 = sdf.parse("2024-01-01 13:00:00");

        System.out.println(date1);
        System.out.println(date2);

        System.out.println(date1.equals(date2));    // false

        Book book1 = new Book("생존코딩", date1, "구웃");        // 지금 날짜
        Book book2 = new Book("생존코딩", date2, "구웃");        // 지금 날짜

        assertEquals(book1, book2);

        Set<Book> books = new HashSet<>();
        books.add(book1);
        books.add(book2);

        assertEquals(1, books.size());
    }

    @Test
    @DisplayName("Book 인스턴스를 담고 있는 컬렉션에 대해 Collections.sort() 를 사용하면 출간일이 신상 순서대로 정렬된다")
    void sortTest() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date1 = sdf.parse("2024-01-02 14:30:00");    // 미친듯한 성능의 pc에서 무진장 빠르게 2개 코드가 실행되면서
        Date date2 = sdf.parse("2024-01-01 13:00:00");

        List<Book> books = new ArrayList<>();

        Book book1 = new Book("생존코딩", date1, "구웃");        // 지금 날짜
        Book book2 = new Book("생존코딩", date2, "구웃");        // 지금 날짜
        books.add(book1);
        books.add(book2);

        Collections.sort(books);

        assertEquals(book1, books.get(0));
    }

    @Test
    @DisplayName("clone() 메서드를 제공하고, 깊은 복사를 수행한다")
    void cloneTest() throws ParseException  {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date1 = sdf.parse("2024-01-02 14:30:00");    // 미친듯한 성능의 pc에서 무진장 빠르게 2개 코드가 실행되면서
        Book book1 = new Book("생존코딩", date1, "구웃");        // 지금 날짜

        Book book2 = book1.clone();

        System.out.println(date1);
    }
}