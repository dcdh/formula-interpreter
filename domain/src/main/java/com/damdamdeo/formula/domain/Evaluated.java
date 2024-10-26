package com.damdamdeo.formula.domain;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public record Evaluated(Value value, PositionedAt positionedAt,
                        Supplier<List<Input>> inputs) {
    public Evaluated {
        Objects.requireNonNull(value);
        Objects.requireNonNull(positionedAt);
        Objects.requireNonNull(inputs);
    }

    public Evaluated(final Value value, final PositionedAt positionedAt) {
        this(value, positionedAt, List::of);
    }

    public Evaluated() {
        this(Value.ofNotAvailable(), new PositionedAt(new PositionStart(), new PositionEnd()), List::of);
    }
}