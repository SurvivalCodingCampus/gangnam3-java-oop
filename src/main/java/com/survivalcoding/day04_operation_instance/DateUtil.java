package com.survivalcoding.day04_operation_instance;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {

    private static final String PATTERN = "yyyy-MM-dd";

    private DateUtil() {
        throw new UnsupportedOperationException("인스턴스를 생성할 수 없습니다.");
    }

    public static Date toDate(String source) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN);
        formatter.setLenient(false);

        ParsePosition position = new ParsePosition(0);
        Date date = formatter.parse(source, position);

        if (date == null || position.getIndex() != source.length()) {
            throw new ParseException("잘못된 날짜 형식입니다: " + source, position.getIndex());
        }

        return date;
    }

    public static String toString(Date date) {
        return new SimpleDateFormat(PATTERN).format(date);
    }
}
