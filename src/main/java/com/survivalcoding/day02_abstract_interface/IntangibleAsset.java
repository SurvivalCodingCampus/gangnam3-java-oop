package com.survivalcoding.day02_abstract_interface;

import java.time.LocalDate;

public abstract class IntangibleAsset extends Asset {

    // field
    private LocalDate expiryDate;

    // getter
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    // setter
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}
