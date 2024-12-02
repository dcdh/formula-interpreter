package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;

import java.util.Objects;

public record GivenValue(Value value) {
    public GivenValue {
        Objects.requireNonNull(value);
    }
}
