package com.damdamdeo.formula.domain;

import java.util.Objects;

public record StructuredReference(Reference reference, Value value) {
    public StructuredReference {
        Objects.requireNonNull(reference);
        Objects.requireNonNull(value);
    }

    public StructuredReference(final Reference reference, final String value) {
        this(reference,
                value != null ? Value.of(value) : Value.ofNotAvailable());
    }

}
