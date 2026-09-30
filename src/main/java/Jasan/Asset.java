package Jasan;

public interface Asset extends Thing {

    String name();

    int price();

    @Override
    double getWeight(); // 확인용으로 만들어 본 것
}


