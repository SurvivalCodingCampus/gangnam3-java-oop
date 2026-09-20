package com.survivalcoding.day04.exam;

import java.util.Random;

public class Utils {
    private static final Random RANDOM = new Random();

    /***
     * @return num포함 0~num 랜던 반환
     */
    public static int nextInt(int num) {
        return RANDOM.nextInt(num + 1);
    }

    public static boolean isValidName(final String name) {
        if (name == null || name.isBlank()) {
            return false;
        }

        return true;
    }

    /**
     * 주어진 값이 최소값과 최대값 범위 내에 있는지 확인 (경계값 포함)
     *
     * @param value
     * @param max
     * @param min
     * @return 값이 범위 내에 있으면 true, 아니면 false
     */
    public static boolean isWithinRange(final int value, final int max, final int min) {
        return min <= value && value <= max;
    }

    public static boolean isWithinRange(final double value, final double max, final double min) {
        return min <= value && value <= max;
    }
}
