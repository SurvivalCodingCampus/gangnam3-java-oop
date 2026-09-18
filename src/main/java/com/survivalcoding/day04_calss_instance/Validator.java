package com.survivalcoding.day04_calss_instance;

public class Validator {
    public static void validate(String name) {
    }
    
    public static void validateNull(String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름은 null이 아니어야 함");
        }
    }
    
    public static void validateMinLength(String name, int minLength) {  // 이하면 날리기
        if (name.length() <= minLength) {
            throw new IllegalArgumentException("이름은 %d 글자를 초과해야 함".formatted(minLength));
        }
    }
    
    public static void validateMaxLength(String name, int maxLength) {  // 이상이면 날리기
        if (name.length() >= maxLength) {
            throw new IllegalArgumentException("이름은 %d 글자 미만이어야 함".formatted(maxLength));
        }
    }
}
