package com.survivalcoding.day04_operation_instance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {

    private static final String PATTERN = "yyyy-MM-dd";

    private DateUtil() {
        throw new UnsupportedOperationException("인스턴스를 생성할 수 없습니다.");
    }

    public static Date toDate(String source) throws ParseException {
        return new SimpleDateFormat(PATTERN).parse(source);
    }

    public static String toString(Date date) {
        return new SimpleDateFormat(PATTERN).format(date);
    }
}
