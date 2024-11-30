package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;

import java.util.Objects;

public record GivenArgument(Value value) {
    public GivenArgument {
        Objects.requireNonNull(value);
    }
}
