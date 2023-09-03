package com.damdamdeo.formula.domain;

import java.util.Map;
import java.util.Objects;

public record ContextualResult(Value result,
                               Map<InputName, Input> inputs,
                               Position position) implements Result {
    public ContextualResult {
        Objects.requireNonNull(result);
        Objects.requireNonNull(inputs);
        Objects.requireNonNull(position);
    }

    public ContextualResult(final Value result, final Position position) {
        this(result, Map.of(), position);
    }

    @Override
    public String value() {
        return result.value();
    }
}
