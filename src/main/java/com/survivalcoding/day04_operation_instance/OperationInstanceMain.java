package com.survivalcoding.day04_operation_instance;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OperationInstanceMain {

    public static void main(String[] args) throws ParseException {
        List<Hero> heroesList = new ArrayList<>();
        Hero h1 = new Hero("슈퍼맨", 100, new Sword());
        Hero h2 = new Hero("슈퍼맨", 100, new Sword());

        heroesList.add(h1);
        System.out.println(heroesList.size()); // 1

        heroesList.remove(h2);
        System.out.println(heroesList.size()); // 1

        Set<Hero> heroesSet = new HashSet<>();
        Hero h3 = new Hero("슈퍼맨", 100, new Sword());
        Hero h4 = new Hero("슈퍼맨", 100, new Sword());

        heroesSet.add(h3);
        System.out.println(heroesSet.size()); // 1

        heroesSet.remove(h4);
        System.out.println(heroesSet.size()); // 1

        System.out.println(h1.toString());

        List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3));

        // Collections.sort(numbers);

        // Collections.sort(numbers, new Comparator<Integer>() {
        // @Override
        // public int compare(Integer o1, Integer o2) {
        // return o2 - o1;
        // }
        // });

        Collections.sort(numbers, (o1, o2) -> o2 - o1);

        System.out.println(numbers);

        List<String> names = new ArrayList<>(Arrays.asList("가", "나", "다"));

        Collections.sort(names, (o1, o2) -> {
            return o1.compareTo(o2);
            // return o1.compareTo(o2) * -1;
        });

        System.out.println(names);

        Hero h5 = new Hero("홍길동", 100, new Sword());
        Hero h6 = h5;

        System.out.println(h5 == h6);
        System.out.println(h5.equals(h6));

        Hero h7 = new Hero("홍길동", 100, new Sword());
        Hero h8 = h7.clone();

        System.out.println(h7 == h8);
        System.out.println(h7.equals(h8));

        // Book
        System.out.println("========== Book 클래스 연습 문제 ==========");

        Book book1 = new Book("이펙티브 자바", DateUtil.toDate("2018-11-01"), "이펙티브 자바");
        Book book2 = new Book("혼자 공부하는 자바", DateUtil.toDate("2024-02-01"), "혼자 공부하는 자바");
        Book book3 = new Book("혼자 공부하는 자바", DateUtil.toDate("2024-02-01"), "혼공자");
        Book book4 = book3.clone();

        System.out.println("\n동일성(book1/book2): " + (book1 == book2));
        System.out.println("동등성(book1/book2): " + book1.equals(book2));

        System.out.println("\n동일성(book2/book3): " + (book2 == book3));
        System.out.println("동등성(book2/book3): " + book2.equals(book3));

        System.out.println("\n동일성(book3/book4): " + (book3 == book4));
        System.out.println("동등성(book3/book4): " + book3.equals(book4));

        List<Book> bookList = new ArrayList<>(Arrays.asList(book1, book2, book3));
        Set<Book> bookSet = new HashSet<>();
        Map<Book, String> bookMap = new HashMap<>();

        bookSet.add(book1);
        bookSet.add(book2);
        bookSet.add(book3);

        bookMap.put(book1, book1.getComment());
        bookMap.put(book2, book2.getComment());
        bookMap.put(book3, book3.getComment());

        System.out.println("\n========== Book List ==========");
        Collections.sort(bookList);
        bookList.forEach((book) -> System.out.println(book));

        System.out.println("\n========== Book Set ==========");
        System.out.println("bookSet Size: " + bookSet.size());
        bookSet.forEach((book) -> System.out.println(book));

        System.out.println("\n========== Book Map ==========");
        System.out.println("bookMap Size" + bookMap.size());
        bookMap.forEach((key, book) -> System.out.println(key + ": " + book));
    }
}