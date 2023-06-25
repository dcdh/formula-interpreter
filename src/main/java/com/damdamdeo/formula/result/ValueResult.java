package com.damdamdeo.formula.result;

import com.damdamdeo.formula.structuredreference.Value;

import java.util.Objects;

public final class ValueResult implements Result {
    private final Value value;

    public ValueResult(final Value value) {
        this.value = Objects.requireNonNull(value);
    }

    public ValueResult(final String value) {
        this(new Value(value));
    }

    @Override
    public String result() {
        return value.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValueResult that = (ValueResult) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
