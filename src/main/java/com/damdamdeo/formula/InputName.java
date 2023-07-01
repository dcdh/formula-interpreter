package com.damdamdeo.formula;

public record InputName(String name) {
    @Override
    public String toString() {
        return "InputName{" +
                "name='" + name + '\'' +
                '}';
    }
}
