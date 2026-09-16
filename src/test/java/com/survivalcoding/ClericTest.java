package com.survivalcoding;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ClericTest {
    @Test
    void healCheck(){
Cleric C=new Cleric();
C.selfAid();
assertEquals(100,C.HP);
    }
    @Test
    void prayCheck(){
      Cleric C=new Cleric();
      C.pray(66666);
      assertEquals(67,C.MP);
    }
    @Test
    void prayCheck2(){
        Cleric C=new Cleric();
        C.pray(16);
        assertEquals(100,C.MP);
    }
    }
