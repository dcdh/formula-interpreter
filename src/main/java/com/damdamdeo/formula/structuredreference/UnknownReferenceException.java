package com.damdamdeo.formula.structuredreference;

import java.util.Objects;

public final class UnknownReferenceException extends RuntimeException {
    private final Reference unknownReference;

    public UnknownReferenceException(final Reference unknownReference) {
        this.unknownReference = Objects.requireNonNull(unknownReference);
    }
}
