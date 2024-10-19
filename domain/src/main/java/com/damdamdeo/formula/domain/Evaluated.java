package com.damdamdeo.formula.domain;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public record Evaluated(Value value, Range range,
                        Supplier<List<Input>> inputs) {
    public Evaluated {
        Objects.requireNonNull(value);
        Objects.requireNonNull(range);
        Objects.requireNonNull(inputs);
    }

    public Evaluated(final Value value, final Range range) {
        this(value, range, List::of);
    }

    public Evaluated() {
        this(Value.ofNotAvailable(), new Range(new RangeStart(), new RangeEnd()), List::of);
    }
}