package com.survivalcoding.day09.Collection;

import java.util.ArrayList;
import java.util.List;

public class StudentMain {
    public static void main(String[] args) {
        final int initCapacity = 2;

        List<Student> studentList = new ArrayList<>(initCapacity);

        studentList.add(new Student("홍길동"));
        studentList.add(new Student("한석봉"));

        for (Student student : studentList) {
            System.out.println(student.getName());
        }
    }
}
