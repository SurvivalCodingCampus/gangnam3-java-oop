package com.survivalcoding;

public class bank {
    void noValue(double x) {
        //throw IllegalArgumentException()
        //가 아니라 throw new IllegalArgument Exception이 되야 한다.
        //return 빈칸은 그냥 void 라 해도 된다.
        if (x < 0) {
            System.out.println("유효하지 않은 금액입니다");
            return;
        }
    }

    abstract class Account {
        private double balance;
        private int INITDRAWCOUNT=0;
        private int drawcount=INITDRAWCOUNT;

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public int getDrawcount() {
            return drawcount;
        }

        //deposit money 입급하다 withdraw money 출금하다.
        void deposit(double x) {
            noValue(x);
            balance += x;
        }
        abstract void withdraw(double x);
    }

    interface InterestBearing{void applyInterest();}
    class SavingsAccount extends Account implements  InterestBearing{
        @Override
        void withdraw(double x) {
            if(getDrawcount()>3){
                System.out.println("월 출금 회수를 초과했습니다");
            }
            else{



            }
        }
    }
    class CheckingAccout extends Account implements  InterestBearing{

    }





}

