package com.survivalcoding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class WandTest {

    @Test
    void setName() {
        Wand wand = new Wand();

        assertThrows(
                IllegalArgumentException.class, () -> wand.setName(null)
        );


    }

    @Test
    void setPower() {
        Wand wand = new Wand();

        assertThrows(
                IllegalArgumentException.class, () -> wand.setPower(1000)
        );


    }
}