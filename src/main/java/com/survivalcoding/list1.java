package com.survivalcoding;

import java.util.ArrayList;
import java.util.List;

// angle brackets <> round brakets () square brakets [] curly brackets {}
//angle brackets <> round brackets () square brackets [] curly brackets {} {} curly brakets [] square brackets () round bracketes {} curly brackets
public class list1 {
    public static void main(String[] args) {
        List<Integer> LAYER = new ArrayList<>();
        List<String> names = new ArrayList<>();
        //under boxing so the list itself does the jon for example int x = b.get(0) get 0 returns and Integer which is changed into a int which we call unboxing on the onther side when we do b.add(5) 5 int get changed into a Interger which we call autoboxing

        //so we got two types of changing autoboxing and unboxing for example of autoboxing if we do LAYER.add(3) 3 is a int but it automatically changes the int into a Integer
        List<String> Fruits = new ArrayList<>();
        Fruits.add("banna");
        Fruits.add("apple");
        Fruits.add(1, "orange");
        Fruits.addAll(List.of("java", "Python"));
        Fruits.get(0);
        Fruits.indexOf(5);
        for (String S : Fruits) {
            System.out.println(S);
        }
        //in concurrent dynamic lists unexpected errors could happen
    }

}
