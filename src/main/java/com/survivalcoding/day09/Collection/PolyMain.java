package com.survivalcoding.day09.Collection;

import java.util.ArrayList;
import java.util.List;

public class PolyMain {
    public static void main(String[] args) {

        A a = new A();
        B b = new B();

        List<Y> yList = new ArrayList<Y>();

        yList.add(a);
        yList.add(b);

        for (Y y : yList) {
            y.b();
        }
    }
}

class A extends Y {

    @Override
    public void a() {
        System.out.println("Aa");
    }

    @Override
    public void b() {
        System.out.println("Ab");
    }

    public void c() {
        System.out.println("Ac");
    }
}

class B extends Y {

    @Override
    public void a() {
        System.out.println("Ba");
    }

    @Override
    public void b() {
        System.out.println("Bb");
    }

    public void c() {
        System.out.println("Bc");
    }
}

abstract class Y implements X {
    public abstract void a();
    public abstract void b();
}

interface X {
    void a();
}
