package com.damdamdeo.formula.domain;

import java.util.Objects;

public record StructuredReference(ReferenceNaming referenceNaming, Value value) {
    public StructuredReference {
        Objects.requireNonNull(referenceNaming);
        Objects.requireNonNull(value);
    }

    public StructuredReference(final ReferenceNaming referenceNaming, final String value) {
        this(referenceNaming,
                value != null ? Value.ofAny(value) : Value.ofNotAvailable());
    }

}
