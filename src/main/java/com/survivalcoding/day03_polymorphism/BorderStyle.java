package com.survivalcoding.day03_polymorphism;

public final class BorderStyle {

    public static final int DASHED = 0;
    public static final int DOTTED = 1;

    private BorderStyle() {
        throw new UnsupportedOperationException("BorderStyle 클래스는 인스턴스를 생성할 수 없습니다.");
    }
}
