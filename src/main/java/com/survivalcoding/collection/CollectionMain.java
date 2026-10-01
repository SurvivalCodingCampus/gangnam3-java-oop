package com.survivalcoding.collection;

import java.util.ArrayList;
import java.util.HashMap;

public class CollectionMain {
    public static void main(String[] args) {
        Student student1 = new Student("홍길동");
        Student student2 = new Student("한석봉");
        
        String name;
        int age;
        
//        연습 문제 2
        ArrayList<Student> studentList = new ArrayList<>();
        studentList.add(student1);
        studentList.add(student2);

        for (int i = 0; i < studentList.size(); i++) {
            name = studentList.get(i).getName();
            int studentNumber = i + 1;
            System.out.printf("%d번 학생 이름 : %s%n", studentNumber, name);
        }
        
//        연습 문제 3
        student1.setAge(20);
        student2.setAge(25);
        
        HashMap<String, Integer> studentMap = new HashMap<>();
        
        for (Student student : studentList) {
            name = student.getName();
            age = student.getAge();
            
            studentMap.put(name, age);
        }
        
        for (String key: studentMap.keySet()) {
            age = studentMap.get(key);
            System.out.printf("%s의 나이는 %d살%n", key, age);
        }
    }
}
