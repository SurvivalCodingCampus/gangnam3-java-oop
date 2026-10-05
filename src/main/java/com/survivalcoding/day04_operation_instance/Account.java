package com.survivalcoding.day04_operation_instance;

public class Account implements Comparable<Account> {

    int number;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + number;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Account account)) {
            return false;
        }
        return this.number == account.number;
    }

    @Override
    public int compareTo(Account obj) {
        if (this.number < obj.number) {
            return -1;
        }
        if (this.number > obj.number) {
            return 1;
        }
        return 0;
    }
}
