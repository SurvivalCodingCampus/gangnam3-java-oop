package com.survivalcoding.collection;

import java.util.HashSet;
import java.util.Set;

public class Student {
    static final int DEFAULT_AGE = 0;
    
    private String name;
    
    private int age;
    
    public Student(String name) {
        this(name, DEFAULT_AGE);
    }
    
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public static void main(String[] args) {
        String a = "a";
        String b = "a";
        
        Set<String> sets = new HashSet<>();
        sets.add(a);
        sets.remove(b);
        
        System.out.println(sets.size());  // 0
        
        // 왜 list는 해시코드로 안하고 set, map만 해시코드로 해??
        // 해시코드 재정의는 어떻게 해? 리스트는 해시코드 재정의해도 같은 걸로 인식 안하나?
        
    }
}
