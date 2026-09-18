package com.survivalcoding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WizardTest {

    @Test
    void setName() {
        Wizard wizard = new Wizard();

        assertThrows(
                IllegalArgumentException.class, () -> wizard.setName(null)
        );
    }

    @Test
    void setHp() {
        Wizard wizard = new Wizard();

        wizard.setHp(-10);

        assertEquals(0, wizard.getHp()); // 숫자 계산할대..?
    }

    @Test
    void setWand() {
        Wizard wizard = new Wizard();
        assertThrows(
                IllegalArgumentException.class, () -> wizard.setWand(null)
        );


    }

    @Test
    void setMp() {
        Wizard wizard = new Wizard();

        assertThrows(
                IllegalArgumentException.class,
                () -> wizard.setMp(-10)
        );
    }
}