package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;

import java.util.Objects;

public record GivenComparison(Value value) {
    public GivenComparison {
        Objects.requireNonNull(value);
    }
}
