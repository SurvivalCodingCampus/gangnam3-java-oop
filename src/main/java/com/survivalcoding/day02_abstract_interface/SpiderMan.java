package com.survivalcoding.day02_abstract_interface;

import java.util.UUID;

public class SpiderMan extends Hero implements Citizen {

    @Override
    public String getResidentId() {
        return UUID.randomUUID().toString();
    }
}
