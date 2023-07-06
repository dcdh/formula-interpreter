package com.damdamdeo.formula.domain;

import java.util.Objects;

public record StructuredDatum(Reference reference, Value value) {
    public StructuredDatum {
        Objects.requireNonNull(reference);
        Objects.requireNonNull(value);
    }

    public StructuredDatum(final Reference reference, final String value) {
        this(reference,
                value != null ? Value.of(value) : Value.ofNotAvailable());
    }

}
