package org.egzi.algo;

public enum AlgoType {
    LSM("LSM"),
    KACZMARZ("KACZMARZ");

    String value;

    AlgoType(String value) {
        this.value = value;
    }
}
