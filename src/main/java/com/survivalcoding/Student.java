package com.survivalcoding;

import java.util.ArrayList;

public class Student {

    static ArrayList<String> names = new ArrayList<String>();

    public static void main(String[] args) {


        names.add("홍길동");
        names.add("한석봉");

        for (int i = 0; i < 2; i++) {
            System.out.println(names.get(i));
        }
    }
}
