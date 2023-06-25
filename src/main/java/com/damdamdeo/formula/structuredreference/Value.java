package com.damdamdeo.formula.structuredreference;

import java.util.Objects;

public record Value(String value) {
    public Value(String value) {
        this.value = Objects.requireNonNull(value);
    }
}
