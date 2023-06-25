package com.damdamdeo.formula.result;

public interface Result {
    String result();
    default boolean isUnknown() {
        return false;
    }
}
