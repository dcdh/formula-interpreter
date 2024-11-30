package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;

import java.util.Objects;

public record Expected(Value value) {
    public Expected {
        Objects.requireNonNull(value);
    }
}