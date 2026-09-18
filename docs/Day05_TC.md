### 테스트케이스

public class Wand {
private String name; // 지팡이의 이름
private double power; // 지팡이의 마력

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름은 null 금지");
        }

        if (name.length() < 3) {
            throw new IllegalArgumentException("이름은 3문자 이상");
        }

        this.name = name;
    }

    public double getPower() {
        return power;
    }

    /**
     * 유효범위 max 100 min 0.5
     */
    public void setPower(final double power) {
        final double maxPower = 100;
        final double minPower = 0.5;

        if (!Utils.isWithinRange(power, maxPower, minPower)) {
            throw new IllegalArgumentException(
                "유효한 범위를 입력해 주세용 max(%f) min(%.1f)"
                    .formatted(maxPower, minPower)
            );
        }

        this.power = power;
    }
}

public class Wand {
private String name; // 지팡이의 이름
private double power; // 지팡이의 마력

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름은 null 금지");
        }

        if (name.length() < 3) {
            throw new IllegalArgumentException("이름은 3문자 이상");
        }

        this.name = name;
    }

    public double getPower() {
        return power;
    }

    /**
     * 유효범위 max 100 min 0.5
     */
    public void setPower(final double power) {
        final double maxPower = 100;
        final double minPower = 0.5;

        if (!Utils.isWithinRange(power, maxPower, minPower)) {
            throw new IllegalArgumentException(
                "유효한 범위를 입력해 주세용 max(%f) min(%.1f)"
                    .formatted(maxPower, minPower)
            );
        }

        this.power = power;
    }
}

public class Person {
private final String name;
private final int birthYear;

    public Person(final String name, final int birthYear) {
        this.name = name;
        this.birthYear = birthYear;
    }

    public int getAge() {
        int thisYear = LocalDate.now().getYear();
        int age = thisYear - birthYear;

        return age;
    }
}

경계값 분석으로해줘 junit으로

이 코드의 테스트 시나리오에 맞는 테스트 코드를 작성해 줘.
작성 시, 우리가 생각하지 못한 부족하거나 보충해야 되는 테스트 케이스가 있다면 그 시나리오만 따로 목록으로 알려 줘.
테스트 코드는 내가 작성한 코드 내에서 검증할 수 있도록 작성해 줘.

Wizard wand
마법사나 지팡이의 이름은 null 일 수 없고, 반드시 3문자 이상이어야 한다
지팡이의 마력은 0.5 이상 100.0 이하여야 한다.
마법사의 지팡이는 null 일 수 없다.
마법사의 MP는 0 이상이어야 한다.
HP가 음수가 되는 상황에서는 대신 0이 설정 되도록 한다.

Person
이름과 태어난 해를 생성자로 받는다 (name, birthYear)
이름과 태어난 해는 한번 정해지면 수정이 불가능하다.
getAge() 메서드를 통해 나이를 제공하지만, 임의로 수정은 불가능하다.
나이 계산은 올해년도에서 birthYear 년도를 뺀 값을 리턴한다