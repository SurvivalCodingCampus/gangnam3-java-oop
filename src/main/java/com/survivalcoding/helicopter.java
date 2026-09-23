package com.survivalcoding;

import java.util.ArrayList;
import java.util.List;

public class helicopter {


    static double caculate_sum_all(List<Employee> E) {
        double sum = 0;
        for (Employee S : E) {
            if (S instanceof Bonusable) {
                sum = sum + S.caculatePay() + ((Bonusable) S).bonus();//casting S into Bonusable type cause compielr doesnt know if S is always bonusable
            } else {
                sum = sum + S.caculatePay();
            }

        }
        return sum;
    }

    static String get_higest(List<Employee> E) {
        Employee high = E.get(0);
        int counter = E.size();
        for (Employee S : E) {
            if (S instanceof Bonusable) {
                S.finalpay = S.caculatePay() + ((Bonusable) S).bonus();//casting S into Bonusable type cause compielr doesnt know if S is always bonusable
            } else {
                S.finalpay = S.caculatePay();
            }

        }
        for (int i = 0; i < E.size(); i++) {
            if (E.get(i).finalpay > high.finalpay) {
                high = E.get(i);
            }

        }
        return high.name;
    }

    public static void main(String[] args) {
        List<Employee> NEW = new ArrayList<>();


    }

    interface Bonusable {
        double bonus();
    }

    abstract static class Employee {
        String name;
        int id;
        double finalpay;

        Employee(String name, int id) {
            this.name = name;
            this.id = id;
        }

        abstract double caculatePay();

    }

    ;

    public static class Fulltime extends Employee implements Bonusable {
        String name;
        int id;
        double bonusE = 0.1;
        double pay = 3000000;
        double finalpay;

        Fulltime(String name, int id) {
            super(name, id);

        }

        public double bonus() {
            return bonusE * pay;
        }

        public double caculatePay() {
            return pay;
        }

    }

    public static class Contractor extends Employee {
        String name;
        int id;
        double pay;
        double time;
        double finalpay;

        Contractor(String name, int id, double pay, double time) {
            super(name, id);
            this.pay = pay;
            this.time = time;
        }

        public double caculatePay() {
            return pay / time;
        }
    }

    public static class PartTime extends Employee implements Bonusable {
        String name;
        int id;
        double pay;
        double time;

        PartTime(String name, int id, double pay, double time) {
            super(name, id);
            this.pay = pay;
            this.time = time;
        }

        public double caculatePay() {
            return pay * time;
        }

        public double bonus() {
            if (time > 40) {
                return (time - 40) * pay;
            } else {
                return 0;
            }
        }


    }


}
