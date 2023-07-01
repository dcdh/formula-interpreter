package com.damdamdeo.formula.structuredreference;

import com.damdamdeo.formula.Value;

import java.util.Objects;

public final class StructuredDatum {

    private final Reference reference;
    private final Value value;
    public StructuredDatum(final Reference reference, final String value) {
        this.reference = Objects.requireNonNull(reference);
        this.value = value != null ? Value.of(value) : Value.ofNotAvailable();
    }

    public Reference reference() {
        return reference;
    }

    public Value value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StructuredDatum that = (StructuredDatum) o;
        return Objects.equals(reference, that.reference) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference, value);
    }
}
