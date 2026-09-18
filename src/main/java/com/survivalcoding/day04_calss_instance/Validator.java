package com.survivalcoding.day04_calss_instance;

public class Validator {
    public static void validate(String name) {
    }
    
    public static void validateNotNull(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("null이 아니어야 함");
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
    
    public static void validateRangeInclusive(double value, double min, double max) {
        validateAtLeast(value, min);
        validateAtMost(value, max);
        
//        if ((value < min) || (value > max)) {
//            throw new IllegalArgumentException("값의 범위는 %f 이상 %f 이하여야 함".formatted(min, max));
//        }
    }
    
    public static void validateAtLeast(double value, double min) {  // min 이상인지 확인
        if (value < min) {
            throw new IllegalArgumentException("값은 %f 이상이어야 함".formatted(min));
        }
    }
    
    public static void validateAtMost(double value, double max) {  // max 이하인지 확인
        if (value > max) {
            throw new IllegalArgumentException("값은 %f 이하여야 함".formatted(max));
        }
    }
}
