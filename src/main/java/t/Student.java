package t;

import java.util.*;


public class Student {
    //list 에 static 붙혀줘야 한다
    static List<String> List = new ArrayList<>();
    Set<String> Set = new HashSet<>();
    Map<String, Integer> Map = new HashMap<>();

    //add get remove 등이 있음
    public static void main(String[] args) {
        List.add("홍길동");
        List.add("한석봉");
        List.forEach(n -> System.out.println(n));
        //람다 함수 함수 이름은 없지만 입력 -> 출력으로 가능.
    }
    //for each 순회가능 iterable 에 가능...


}

