package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;

import java.util.Objects;

public record GivenRight(Value value) {
    public GivenRight {
        Objects.requireNonNull(value);
    }
}