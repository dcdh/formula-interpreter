package com.damdamdeo.formula.structuredreference;

import java.util.Objects;

public record Reference(String reference) {
    public Reference(final String reference) {
        this.reference = Objects.requireNonNull(reference);
    }
}
