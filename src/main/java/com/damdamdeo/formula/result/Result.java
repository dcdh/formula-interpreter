package com.damdamdeo.formula.result;

import com.damdamdeo.formula.Value;

public interface Result {
    Value value();
    default boolean isUnknown() {
        return false;
    }

    default boolean isError() {
        return false;
    }

    default boolean isNumeric() {
        return false;
    }
}
