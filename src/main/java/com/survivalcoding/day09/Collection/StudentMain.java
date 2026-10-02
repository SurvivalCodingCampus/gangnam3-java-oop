package com.survivalcoding.day09.Collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentMain {
    public static void main(String[] args) {

        final int initCapacity = 2;

        // 연습문제 2
        List<Student> studentList = new ArrayList<>(initCapacity);

        Student hong = new Student("홍길동");
        Student han = new Student("한석봉");

        studentList.add(hong);
        studentList.add(han);

        for (Student student : studentList) {
            System.out.println(student.getName());
        }

        System.out.println();

        // 연습문제 3
        Map<Integer, Student> studentMap = new HashMap<>(initCapacity);

        int hongAge = 20;
        int hanAge = 25;

        studentMap.put(hongAge, hong);
        studentMap.put(hanAge, han);

        for (int key: studentMap.keySet()) {
            Student student = studentMap.get(key);

            System.out.printf("%s의 나이는 %d살 \n", student.getName(), key);
        }
    }
}
