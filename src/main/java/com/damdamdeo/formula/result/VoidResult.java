package com.damdamdeo.formula.result;

import com.damdamdeo.formula.Value;

import java.util.Objects;

public final class VoidResult implements Result {
    private final Value value = new Value("");

    @Override
    public Value value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoidResult that = (VoidResult) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
