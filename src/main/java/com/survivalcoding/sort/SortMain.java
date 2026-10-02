package com.survivalcoding.sort;

import com.survivalcoding.day01_class_instance.Hero;

import java.util.*;

public class SortMain {
    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>();

        numbers.add(1);
        numbers.add(3);
        numbers.add(2);

        Collections.sort(numbers);
        System.out.println(numbers);

        List<String> names = new ArrayList<>();
        names.add("김");
        names.add("이");
        names.add("박");
        Collections.sort(names);
        System.out.println(names);

        List<Hero> heroes = new ArrayList<>();
        heroes.add(new Hero("김", 10));
        heroes.add(new Hero("이", 30));
        heroes.add(new Hero("박", 20));
//        Collections.sort(heroes, new MyComparator());

        Collections.sort(heroes, new Comparator<Hero>() {
            @Override
            public int compare(Hero o1, Hero o2) {
                return o1.compareTo(o2) * -1;
            }
        });
        System.out.println(heroes);

        Hero h1 = new Hero("홍길동");
        Hero h2 = h1.clone();

        System.out.println(h1.equals(h2));
    }
}

class MyComparator implements Comparator<Hero> {

    @Override
    public int compare(Hero o1, Hero o2) {
        return o1.compareTo(o2);
    }
}