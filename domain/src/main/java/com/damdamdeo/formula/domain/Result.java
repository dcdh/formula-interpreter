package com.damdamdeo.formula.domain;

import java.util.List;
import java.util.Objects;

public record Result(Value value,
                     List<Input> inputs,
                     Range range) {
    public Result {
        Objects.requireNonNull(value);
        Objects.requireNonNull(inputs);
        Objects.requireNonNull(range);
    }

    public Result(final Value result, final Range range) {
        this(result, List.of(), range);
    }

    public Result() {
        this(Value.ofNotAvailable(), List.of(), new Range(new RangeStart(), new RangeEnd()));
    }
}
