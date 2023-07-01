package com.damdamdeo.formula.structuredreference;

import com.damdamdeo.formula.Input;

import java.util.Objects;

public record Reference(String reference) implements Input {
    public Reference(final String reference) {
        this.reference = Objects.requireNonNull(reference);
    }

    @Override
    public String value() {
        return reference;
    }
}
