package com.survivalcoding.collection;

import java.util.ArrayList;

public class CollectionMain {
    public static void main(String[] args) {
        Student student1 = new Student("홍길동");
        Student student2 = new Student("한석봉");
        
        ArrayList<Student> studentList = new ArrayList<>();
        studentList.add(student1);
        studentList.add(student2);
        
        for (int i = 0; i < studentList.size(); i++) {
            String name = studentList.get(i).getName();
            int studentNumber = i + 1;
            System.out.printf("%d번 학생 이름 : %s%n", studentNumber, name);
        }
    }
}
