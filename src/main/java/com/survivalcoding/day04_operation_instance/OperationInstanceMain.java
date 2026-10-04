package com.survivalcoding.day04_operation_instance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OperationInstanceMain {

    public static void main(String[] args) {
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
    }
}