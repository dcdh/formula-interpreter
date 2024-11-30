package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;

import java.util.Objects;

public record GivenLeft(Value value) {
    public GivenLeft {
        Objects.requireNonNull(value);
    }
}