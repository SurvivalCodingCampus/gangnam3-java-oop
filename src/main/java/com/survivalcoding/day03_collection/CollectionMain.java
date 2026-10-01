package com.survivalcoding.day03_collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionMain {

    public static void main(String[] args) {
        Student student1 = new Student("홍길동", 20);
        Student student2 = new Student("한석봉", 25);

        List<Student> studentList = new ArrayList<>();

        studentList.add(student1);
        studentList.add(student2);

        Map<String, Integer> studentMap = new HashMap<>();

        for (Student student : studentList) {
            System.out.println(student.getName());
            studentMap.put(student.getName(), student.getAge());
        }

        studentMap.forEach((name, age) -> System.out.println(name + "의 나이는 " + age + "살"));
    }
}
