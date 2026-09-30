package com.survivalcoding.day02_abstract_interface;

public interface Thing {

    double MIN_WEIGHT = 0.0;
    double MAX_WEIGHT = 180.0;

    double getWeight();

    void setWeight(double weight);
}
