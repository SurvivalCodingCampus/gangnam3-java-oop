package com.survivalcoding.instance_basic;

import com.survivalcoding.day01_class_instance.Hero;

import java.util.HashSet;
import java.util.Set;

public class InstanceBasicMain {
    public static void main(String[] args) {
        Set<Hero> heroes = new HashSet<>();

        // 같은걸로 취급되게 하고 싶다
        Hero hero1 = new Hero("슈퍼맨");
        Hero hero2 = new Hero("슈퍼맨");

        System.out.println(hero1.hashCode());
        System.out.println(hero2.hashCode());

        heroes.add(hero1);
        heroes.remove(hero2);

        // List : 1 -> equals 재정의 0
        // Set : 1 -> hashCode 1
        System.out.println(heroes.size());

        // 주소 비교
        System.out.println(hero1 == hero2);     // false

        String fullName = "홍길동";

        String firstName = "길동";
        String lastName = "홍";

        System.out.println(fullName.equals(lastName + firstName));  // true

        // 같은걸로 보게 하고 싶어. 모든데이터가 같으면
        System.out.println(hero1.equals(hero2));        // true
    }

}
