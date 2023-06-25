package com.damdamdeo.formula.structuredreference;

import java.util.Objects;

public record StructuredDatum(Reference reference, Value value) {

    public StructuredDatum(final Reference reference, final Value value) {
        this.reference = Objects.requireNonNull(reference);
        this.value = Objects.requireNonNull(value);
    }
}
