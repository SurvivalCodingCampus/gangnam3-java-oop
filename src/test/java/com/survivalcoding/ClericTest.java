package com.survivalcoding;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ClericTest {

    @Test
    @DisplayName("프레이쓰면 어케되는지 확인해야함")
    void pray() {
        final Cleric cleric = new Cleric ();
        cleric.mp = 0;

        cleric.pray(3);

        assertEquals(5 , cleric.mp);


    }


}
