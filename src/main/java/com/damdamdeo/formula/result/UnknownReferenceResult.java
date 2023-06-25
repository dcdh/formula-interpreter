package com.damdamdeo.formula.result;

import com.damdamdeo.formula.structuredreference.Value;

import java.util.Objects;

public final class UnknownReferenceResult implements Result {
    private final Value value = new Value("#REF!");
    @Override
    public Value value() {
        return value;
    }
    @Override
    public boolean isUnknown() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnknownReferenceResult that = (UnknownReferenceResult) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
