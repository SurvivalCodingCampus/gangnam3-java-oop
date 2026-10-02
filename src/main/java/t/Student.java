package t;

import java.util.*;

public class Student {
    //자료구조에는  static 붙혀줘야 한다
    static List<String> List = new ArrayList<>();
    static Map<String, Integer> Map = new HashMap<>();
    Set<String> Set = new HashSet<>();

    //add get remove 등이 있음
    public static void main(String[] args) {
        List.add("홍길동");
        List.add("한석봉");
        List.forEach(n -> System.out.println(n));
        //람다 함수 함수 이름은 없지만 입력 -> 출력으로 가능.
        //list set 은 add map 은 put
        Map.put("홍길동", 20);
        Map.put("한석봉", 25);
        Map.forEach((a, b) -> System.out.println(a + " 나이는" + " " + b + "살"));
    }
    //for each 순회가능 iterable 에 가능.... ..
}

