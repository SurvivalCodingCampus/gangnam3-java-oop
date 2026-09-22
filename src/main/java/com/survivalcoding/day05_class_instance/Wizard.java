package com.survivalcoding.day05_class_instance; // Hero, Wand와 같은 패키지를 사용한다.

public class Wizard { // 외부에서 사용할 수 있는 마법사 클래스를 선언한다.
    private static final int MIN_NAME_LENGTH = 3; // 마법사 이름의 최소 길이임.
    private static final int MIN_HP = 0; // HP의 최솟값이다.
    private static final int MIN_MP = 0; // MP의 최솟값이다.
    private static final int BASE_POINT = 10; // 기본 회복량 포인트는 10 이다.

    private int hp; // HP는 외부에서 직접 수정할 수 없게 숨긴다.
    private int mp; // MP는 setter를 통해 검사 후 변경한다.
    private String name; // 이름은 setter를 통해 검사 후 변경한다.
    private Wand wand; // 장착한 지팡이의 참조를 저장한다.

    public void heal(Hero hero) { // 교재처럼 Hero를 회복 대상으로 받는다.
        if (hero == null) { // 추가 검증: 회복 대상이 없으면 실행을 중단한다.
            throw new IllegalArgumentException("회복 대상은 null일 수 없습니다."); // 잘못된 인수를 알린다.
        } // 대상 검사를 마친다.
        if (wand == null) { // 기본 생성 직후에는 지팡이가 없을 수 있으므로 사용 전에 확인한다.
            throw new IllegalStateException("회복하려면 먼저 지팡이를 설정해야 합니다."); // 마법사의 준비되지 않은 상태를 알린다.
        } // 지팡이 검사를 마친다.
        int recovPoint = (int) (BASE_POINT * wand.getPower()); // 교재대로 기본 회복량에 마력을 곱하고 소수 부분은 버린다. 말그대로 정수로 나옴.
        int healedHp = Math.addExact(hero.getHp(), recovPoint); // 추가 보호: int 범위를 넘으면 잘못된 HP를 저장하지 않고 예외를 낸다.
        hero.setHp(healedHp); // Hero의 setter를 통해 회복한 HP를 저장한다. 교재에는 MP 소비 규칙이 없다.
        System.out.println(hero.getName() + "의 HP를 " + recovPoint + " 회복했다!"); // 교재처럼 회복 대상과 회복량을 출력한다.
    } // heal 메서드를 마친다.

    public int getHp() { // 현재 HP를 읽는다.
        return hp; // 저장된 HP를 반환한다.
    } // getHp 메서드를 마친다.

    public void setHp(int hp) { // HP를 설정한다.
        this.hp = Math.max(hp, MIN_HP); // 음수는 예외 대신 0으로 보정한다.
    } // setHp 메서드를 마친다.

    public int getMp() { // 현재 MP를 읽는다.
        return mp; // 저장된 MP를 반환한다.
    } // getMp 메서드를 마친다.

    public void setMp(int mp) { // MP를 설정한다.
        validateMp(mp); // 음수인지 먼저 검사한다.
        this.mp = mp; // 유효한 MP만 저장한다.
    } // setMp 메서드를 마친다.

    public String getName() { // 마법사의 이름을 읽는다.
        return name; // 저장된 이름을 반환한다.
    } // getName 메서드를 마친다.

    public void setName(String name) { // 마법사의 이름을 설정한다.
        validateName(name); // 이름이 유효한지 먼저 검사한다.
        this.name = name; // 유효한 이름만 저장한다.
    } // setName 메서드를 마친다.

    public Wand getWand() { // 장착한 지팡이를 읽는다.
        return wand; // 지팡이 객체의 참조를 반환한다. 복사본은 아니다.
    } // getWand 메서드를 마친다.

    public void setWand(Wand wand) { // 마법사에게 지팡이를 장착한다.
        validateWand(wand); // null인지 먼저 검사한다.
        this.wand = wand; // 유효한 지팡이 참조만 저장한다.
    } // setWand 메서드를 마친다.

    private void validateMp(int mp) { // MP 검사에 사용하는 내부 도우미다.
        if (mp < MIN_MP) { // 0은 허용하고 음수만 거부한다.
            throw new IllegalArgumentException("MP는 " + MIN_MP + " 이상이어야 합니다."); // 잘못된 MP를 알린다.
        } // MP 검사를 마친다.
    } // validateMp 메서드를 마친다.

    private void validateName(String name) { // 이름 검사에 사용하는 내부 도우미다.
        if (name == null || name.trim().length() < MIN_NAME_LENGTH) { // null과 3글자 미만을 거부한다. trim은 앞뒤 공백만 제거한다.
            throw new IllegalArgumentException( // 잘못된 이름을 거부한다.
                    "마법사 이름은 앞뒤 공백을 제외하고 " + MIN_NAME_LENGTH + "글자 이상이어야 합니다."); // 잘못된 이름을 알린다.
        } // 이름 검사를 마친다.
    } // validateName 메서드를 마친다.

    private void validateWand(Wand wand) { // 지팡이 검사에 사용하는 내부 도우미다.
        if (wand == null) { // null을 장착하는 것을 막는다.
            throw new IllegalArgumentException("지팡이는 null일 수 없습니다."); // 잘못된 지팡이 인수를 알린다.
        } // 지팡이 검사를 마친다.
    } // validateWand 메서드를 마친다.
} // Wizard 클래스를 마친다.
