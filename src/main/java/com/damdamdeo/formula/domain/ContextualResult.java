package com.damdamdeo.formula.domain;

import java.util.Map;
import java.util.Objects;

public record ContextualResult(Value result,
                               Map<InputName, Input> inputs,
                               Range range) implements Result {
    public ContextualResult {
        Objects.requireNonNull(result);
        Objects.requireNonNull(inputs);
        Objects.requireNonNull(range);
    }

    public ContextualResult(final Value result, final Range range) {
        this(result, Map.of(), range);
    }

    @Override
    public String value() {
        return result.value();
    }
}
