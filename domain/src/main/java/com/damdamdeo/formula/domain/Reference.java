package com.damdamdeo.formula.domain;

import java.util.Objects;

public record Reference(String reference) implements InputValue {
    public Reference {
        Objects.requireNonNull(reference);
    }

    @Override
    public String value() {
        return reference;
    }
}
