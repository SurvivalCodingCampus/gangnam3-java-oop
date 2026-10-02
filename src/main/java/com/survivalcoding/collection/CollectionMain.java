package com.survivalcoding.collection;

import java.util.ArrayList;
import java.util.HashSet;

public class CollectionMain {
    public static void main(String[] args) {
        ArrayList<String> names = new ArrayList<>();

        names.add("홍");
        names.add("길");
        names.add("동");
        names.add("홍");

        System.out.println(names);
        System.out.println(names.contains("동"));

        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(10);

        HashSet<String> colors = new HashSet<>();
        colors.add("홍");
        colors.add("길");
        colors.add("동");
        colors.add("홍");
        System.out.println(colors);
        System.out.println(colors.contains("동"));   // 미친듯이 빠르다
    }
}
